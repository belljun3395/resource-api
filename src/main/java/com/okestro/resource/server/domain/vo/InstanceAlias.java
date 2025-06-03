package com.okestro.resource.server.domain.vo;

import java.util.Random;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.lanwen.verbalregex.VerbalExpression;

@EqualsAndHashCode
@ToString
@Getter
public class InstanceAlias {
	private static final String SPLIT_REGEX = "_";
	private static final VerbalExpression REGEX =
			VerbalExpression.regex()
					.startOfLine()
					.then("[a-zA-Z0-9]+") // key part
					.then(SPLIT_REGEX) // underscore separator
					.then("[0-9]{1,4}") // random number part
					.endOfLine()
					.build();
	private final String value;

	private InstanceAlias(String value) {
		this.value = value;
	}

	public static InstanceAlias create(String key) {
		int randomNumber = new Random().nextInt(10000);
		String aliasName = key + SPLIT_REGEX + randomNumber;
		validateAliasName(aliasName);
		return new InstanceAlias(aliasName);
	}

	public static InstanceAlias exist(String aliasName) {
		validateAliasName(aliasName);
		return new InstanceAlias(aliasName);
	}

	private static void validateAliasName(String aliasName) {
		if (aliasName == null || aliasName.isEmpty()) {
			throw new IllegalArgumentException("AliasName cannot be null or empty");
		}
		if (aliasName.length() > 255) {
			throw new IllegalArgumentException(
					"AliasName length must be less than or equal to 255 characters");
		}
		if (!REGEX.testExact(aliasName)) {
			throw new IllegalArgumentException(
					"AliasName must match the format: key_randomNumber (e.g., instance_1234)");
		}
	}
}
