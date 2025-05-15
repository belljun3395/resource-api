CREATE
USER 'resources-local'@'localhost' IDENTIFIED BY 'resources-local';
CREATE
USER 'resources-local'@'%' IDENTIFIED BY 'resources-local';

GRANT ALL PRIVILEGES ON *.* TO
'resources-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
'resources-local'@'%';

CREATE
DATABASE resources_api DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
