INSERT INTO roles (role_name) VALUES ('USER');
INSERT INTO roles (role_name) VALUES ('ADMIN');
INSERT INTO roles (role_name) VALUES ('MODERATOR');
INSERT INTO roles (role_name) VALUES ('EMPLOYEE');
INSERT INTO properties_product (properties_product_name) VALUES ('INGREDIENS');/*ингридиенты*/
INSERT INTO properties_product (properties_product_name) VALUES ('CONSUMABLES');/*расходные материалы*/
INSERT INTO properties_product (properties_product_name) VALUES ('PRODUCT_COMPOSITE');/*конечный продукт & ингридиент*/
INSERT INTO properties_product (properties_product_name) VALUES ('PRODUCT_FINAL');/*продукт на продажу*/
INSERT INTO products_measure (measure_product) VALUES ('LITER');/*литры*/
INSERT INTO products_measure (measure_product) VALUES ('KILOGRAM');/*килограммы*/
INSERT INTO products_measure (measure_product) VALUES ('UNIT');/*единицы*/
INSERT INTO consignments_status (status_name) VALUES ('ARRIVAL');/*приход*/
INSERT INTO consignments_status (status_name) VALUES ('CONSAMPTION');/*расход*/
INSERT INTO consignments_status (status_name) VALUES ('RETURN');/*возврат*/
INSERT INTO consignments_status (status_name) VALUES ('WRITE-OFF');/*списание*/