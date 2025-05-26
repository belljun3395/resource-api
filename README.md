# RESOURCE API Project

## 임시 리드미

**!! 아래 스크립트는 루트 디렉토리를 기준으로 합니다 !!**

### 실행을 위한 로컬 개발 환경 설정

docker 및 docker-compose 설치가 필요합니다.

```bash
cd ./scripts && bash local-develop-env-reset && cd ..
```

### 로컬 개발 환경 실행

```bash
# build
./gradlew bootJar
```

```bash
# run
 java -jar ./build/libs/resource-0.0.1-SNAPSHOT.jar --spring.profiles.active=local,new
```

### API 문서

```bash
# API 문서 생성
./gradlew generateOpenApiDocs
```

```bash
# API 문서 복사
cp ./build/openapi.json ./
```

### 테스트 실행

```bash
./gradlew test
```
