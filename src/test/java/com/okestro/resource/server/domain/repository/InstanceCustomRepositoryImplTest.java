package com.okestro.resource.server.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.okestro.resource.config.FlywayConfig;
import com.okestro.resource.config.JpaDataSourceConfig;
import com.okestro.resource.server.domain.InstanceEntity;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(
		properties = {
			"spring.config.location=classpath:/application-test-data.yml",
			"logging.config=classpath:logback-spring-test-data.xml"
		})
@Import({InstanceCustomRepositoryImpl.class, FlywayConfig.class, JpaDataSourceConfig.class})
class InstanceCustomRepositoryImplTest {
	private static final Long TEST_DATA_COUNT_100_TO_110 = (long) (100 + new Random().nextInt(11));
	private static final Integer TEST_PAGE_SIZE_100_TO_110 = 10;
	private static final Integer TEST_LAST_PAGE_LEFT_ELEMENTS_100_TO_110 =
			TEST_DATA_COUNT_100_TO_110.intValue() % TEST_PAGE_SIZE_100_TO_110;

	@Qualifier("instanceCustomRepositoryImpl")
	@Autowired
	private InstanceCustomRepository instanceCustomRepository;

	@BeforeAll
	static void setUp(@Autowired DataSource integrationWriteDataSource) throws Exception {
		String content = Files.readString(Paths.get("src/main/resources/db/test/init.data.sql"));

		List<String> deleteStatements = new ArrayList<>();
		Pattern deletePattern =
				Pattern.compile("DELETE FROM\\s+\\w+.*?;", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher deleteMatcher = deletePattern.matcher(content);
		while (deleteMatcher.find()) {
			deleteStatements.add(deleteMatcher.group());
		}

		Pattern procPattern =
				Pattern.compile("DELIMITER\\s+//\\s*CREATE PROCEDURE.*?END;\\s*\\R?\\s*//", Pattern.DOTALL);
		Matcher matcher = procPattern.matcher(content);

		List<String> insertStatements = new ArrayList<>();
		Pattern insertPattern = Pattern.compile("INSERT INTO\\s+[\\s\\S]+?;\\s*", Pattern.DOTALL);

		while (matcher.find()) {
			String procedureBody = matcher.group();
			Matcher insertMatcher = insertPattern.matcher(procedureBody);
			while (insertMatcher.find()) {
				insertStatements.add(insertMatcher.group());
			}
		}

		try (Connection connection = integrationWriteDataSource.getConnection();
				Statement statement = connection.createStatement()) {
			for (String deleteSql : deleteStatements) {
				statement.execute(deleteSql);
			}

			for (String insertTemplate : insertStatements) {
				for (int i = 1; i <= TEST_DATA_COUNT_100_TO_110; i++) {
					String insertSql = insertTemplate.replaceAll("\\bi\\b", String.valueOf(i));
					statement.execute(insertSql);
				}
			}
		}
	}

	@ParameterizedTest(name = "{index} - 페이지 {0}  크기 {1} 요소 개수 {2} 검색 테스트")
	@MethodSource("searchPageProvider")
	void search_page(int pageNumber, int pageSize, int numberOfElements) {
		// given
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		// when
		Page<InstanceEntity> resultPage = instanceCustomRepository.search(pageable);

		// then
		int totalPages = (int) Math.ceil((double) TEST_DATA_COUNT_100_TO_110 / pageSize);
		assertThat(resultPage).isNotNull();
		assertThat(resultPage.getTotalPages()).isEqualTo(totalPages); // 전체 페이지 수: 총 요소 개수 / 페이지 크기
		assertThat(resultPage.getTotalElements()).isEqualTo(TEST_DATA_COUNT_100_TO_110); // 전체 요소 개수
		assertThat(resultPage.getNumber()).isEqualTo(pageNumber); // 현재 페이지 번호
		assertThat(resultPage.getSize()).isEqualTo(pageSize); // 현재 페이지 크기
		assertThat(resultPage.getNumberOfElements()).isEqualTo(numberOfElements); // 현재 페이지 요소 개수
	}

	static List<Object[]> searchPageProvider() {
		int totalPages =
				(int) Math.ceil((double) TEST_DATA_COUNT_100_TO_110 / TEST_PAGE_SIZE_100_TO_110);
		List<Object[]> params = new ArrayList<>();
		// until the last page, all pages have the same size
		for (int i = 0; i < totalPages - 1; i++) {
			params.add(new Object[] {i, TEST_PAGE_SIZE_100_TO_110, TEST_PAGE_SIZE_100_TO_110});
		}
		// the last page may have fewer elements
		params.add(
				new Object[] {
					totalPages - 1, TEST_PAGE_SIZE_100_TO_110, TEST_LAST_PAGE_LEFT_ELEMENTS_100_TO_110
				});
		// over total pages, it should return an empty page
		params.add(new Object[] {totalPages, TEST_PAGE_SIZE_100_TO_110, 0});
		return params;
	}
}
