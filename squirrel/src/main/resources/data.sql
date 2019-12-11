INSERT INTO roles (role_id, role_name) VALUES (1 ,'USER_ROLE');
INSERT INTO roles (role_id, role_name) VALUES (2, 'ADMIN');
INSERT INTO roles (role_id, role_name) VALUES (3 ,'MODERATOR');
INSERT INTO roles (role_id, role_name) VALUES (4,'EMPLOYEE');
INSERT INTO roles (role_id, role_name) VALUES (5,'EMPLOYEE_WITH_ACCESS');
INSERT INTO properties_product (properties_product_Id, properties_product_name) VALUES (1, 'INGREDIENS');/*ингридиенты*/
INSERT INTO properties_product (properties_product_Id, properties_product_name) VALUES (2, 'CONSUMABLES');/*расходные материалы*/
INSERT INTO properties_product (properties_product_Id, properties_product_name) VALUES (3, 'PRODUCT_COMPOSITE');/*конечный продукт & ингридиент*/
INSERT INTO properties_product (properties_product_Id, properties_product_name) VALUES (4 , 'PRODUCT_FINAL');/*продукт на продажу*/
INSERT INTO products_measure (measure_product_Id, measure_product) VALUES (1,'LITER');/*литры*/
INSERT INTO products_measure (measure_product_Id, measure_product) VALUES (2,'KILOGRAM');/*килограммы*/
INSERT INTO products_measure (measure_product_Id, measure_product) VALUES (3,'UNIT');/*единицы*/
INSERT INTO consignments_status (consignment_status_id, status_name) VALUES (1,'ARRIVAL');/*приход*/
INSERT INTO consignments_status (consignment_status_id, status_name) VALUES (2, 'CONSAMPTION');/*расход*/
INSERT INTO consignments_status (consignment_status_id, status_name) VALUES (3, 'RETURN');/*возврат*/
INSERT INTO consignments_status (consignment_status_id, status_name) VALUES (4, 'WRITE-OFF');/*списание*/