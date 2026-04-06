#Comandos Linux
sudo apt update
sudo apt install mysql-server
sudo mysql -u root -p

#Comandos previos en MYSQL
CREATE USER 'jsbm'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'jsbm'@'localhost';

#Creación de esquema de base de datos y tablas con sus relaciones
CREATE DATABASE sistema_personas;
USE sistema_personas;

CREATE TABLE ciudades (
    ciud_id INT PRIMARY KEY,
    ciud_nombre VARCHAR(100) NOT NULL
);

CREATE TABLE personas (
    dir_tel VARCHAR(15) PRIMARY KEY,
    dir_tipo_tel VARCHAR(20),
    dir_nombre VARCHAR(100) NOT NULL,
    dir_direccion VARCHAR(150),
    dir_ciud_id INT,
    CONSTRAINT fk_ciudad
        FOREIGN KEY (dir_ciud_id)
        REFERENCES ciudades(ciud_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

INSERT INTO ciudades (ciud_id, ciud_nombre) VALUES
(1, 'Bogotá'),
(2, 'Medellín'),
(3, 'Cali'),
(4, 'Barranquilla'),
(5, 'Cartagena');

INSERT INTO personas (dir_tel, dir_tipo_tel, dir_nombre, dir_direccion, dir_ciud_id) VALUES
('3001234567', 'Celular', 'Juan Pérez', 'Calle 10 #20-30', 1),
('3012345678', 'Celular', 'María Gómez', 'Carrera 15 #45-60', 2),
('3023456789', 'Fijo', 'Carlos López', 'Avenida 3 #12-50', 3),
('3034567890', 'Celular', 'Ana Martínez', 'Diagonal 25 #33-40', 4),
('3045678901', 'Fijo', 'Luis Rodríguez', 'Transversal 8 #22-10', 5);

SELECT 
    p.dir_tel,
    p.dir_nombre,
    p.dir_direccion,
    c.ciud_nombre
FROM personas p
INNER JOIN ciudades c ON p.dir_ciud_id = c.ciud_id;