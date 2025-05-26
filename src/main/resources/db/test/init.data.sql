-- 변수 선언
SET @data_count = 5;

    -- 기존 데이터 모두 삭제
DELETE FROM images where id <= 1000;
DELETE FROM flavors where id <= 1000;
DELETE FROM instances where id <= 1000;

-- images 초기 데이터 삽입
DELIMITER //
CREATE PROCEDURE insert_images()
BEGIN
  DECLARE i INT DEFAULT 1;
  WHILE i <= @data_count DO
    INSERT INTO images (id, name, created_at)
    VALUES (i, CONCAT('image_', i), NOW());
    SET i = i + 1;
END WHILE;
END;
//
DELIMITER ;

CALL insert_images();
DROP PROCEDURE insert_images;

-- flavors 초기 데이터 삽입
DELIMITER //
CREATE PROCEDURE insert_flavors()
BEGIN
  DECLARE i INT DEFAULT 1;
  WHILE i <= @data_count DO
    INSERT INTO flavors (id, name, description, vCPU, memory, root_disk_size, created_at)
    VALUES (
      i,
      CONCAT('flavor_', i),
      CONCAT('description_', i),
      1.0 + i * 0.5,
      1024 * i,
      10 * i,
      NOW()
    );
    SET i = i + 1;
END WHILE;
END;
//
DELIMITER ;

CALL insert_flavors();
DROP PROCEDURE insert_flavors;

-- instances 초기 데이터 삽입
DELIMITER //
CREATE PROCEDURE insert_instances()
BEGIN
  DECLARE i INT DEFAULT 1;
  WHILE i <= @data_count DO
    INSERT INTO instances (
      id, name, alias, description, power_status, host, flavor_id,
      source_type, source_target_id, created_at, updated_at, deleted
    )
    VALUES (
      i,
      CONCAT('instance_', i),
      CONCAT('alias_', i),
      'description',
      'RUNNING',
      CONCAT('192.168.0.', i),
      i,
      'IMAGE',
      i,
      NOW(),
      NOW(),
      FALSE
    );
    SET i = i + 1;
END WHILE;
END;
//
DELIMITER ;

CALL insert_instances();
DROP PROCEDURE insert_instances;
