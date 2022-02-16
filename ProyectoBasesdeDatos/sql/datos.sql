# Carga de datos de Prueba

# Ciudad (cod_postal,nombre)
INSERT INTO Ciudad VALUES (6409,"Tres Lomas");
INSERT INTO Ciudad VALUES (8000,"Bahia Blanca");
INSERT INTO Ciudad VALUES (1850,"Dorrego");

# Sucursal (nro_suc AUTO_INCREMENT,nombre,direccion,telefono,horario,cod_postal)
INSERT INTO Sucursal (nombre,direccion,telefono,horario,cod_postal) VALUES ("Banco Provincia","Alem 979","239212345","8hs a 15hs",8000);
INSERT INTO Sucursal (nombre,direccion,telefono,horario,cod_postal) VALUES ("Banco Nacion","Monteverde 105","239200000","8hs a 14hs",6409);

# Empleado (legajo AUTO_INCREMENT,apellido,nombre,tipo_doc,nro_doc,direccion,telefono,cargo,password,nro_suc)
INSERT INTO Empleado (apellido,nombre,tipo_doc,nro_doc,direccion,telefono,cargo,password,nro_suc) VALUES ("Martinez", "Pity", "DNI Ejemplar A",41123456, "Corrientes 423","29109122018","Jefe",md5('pwpity'),1);
INSERT INTO Empleado (apellido,nombre,tipo_doc,nro_doc,direccion,telefono,cargo,password,nro_suc) VALUES ("Quintero", "Juan Fer", "DNI Ejemplar A",41987654, "Madrid 912","291430001","Cajero",md5('pwjuanfer'),2);

# Cliente (nro_cliente AUTO_INCREMENT, apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac)
INSERT INTO Cliente (apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ("De Battista","Agustin","DNI","42091234","Casanova 473","2921495069","1999/11/23");
INSERT INTO Cliente (apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ("Escobar","Agustin","DNI","42091202","Alem 473","2921501020","1999/04/05");
INSERT INTO Cliente (apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ("Pereyra","Roberto","DNI","38011409","12 de Octubre 1000","2915012389","1990/12/10");
INSERT INTO Cliente (apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES ("De Paul","Rodrigo","DNI","30011900","Salta 200","2921490303","1995/02/25");

# Plazo_fijo (nro_plazo AUTO_INCREMENT,capital,fecha_inicio,fecha_fin,tasa_interes,interes,nro_suc)
INSERT INTO Plazo_Fijo (capital,fecha_inicio,fecha_fin,tasa_interes,interes,nro_suc) VALUES (15345,"2020/06/22","2021/09/18",30,25,1);
INSERT INTO Plazo_fijo (capital,fecha_inicio,fecha_fin,tasa_interes,interes,nro_suc) VALUES (12000,"2012/12/12","2014/01/01",15,5,2);

# Tasa_plazo_fijo (periodo, monto_inf, monto_sup, tasa)
INSERT INTO Tasa_plazo_fijo VALUES (100,2300,1000,15);
INSERT INTO Tasa_plazo_fijo VALUES (102,2350,1020,10);
INSERT INTO Tasa_plazo_fijo VALUES (400,1000,1015,5);
INSERT INTO Tasa_plazo_fijo VALUES (103,2302,1090,30);

# Plazo_cliente (nro_plazo, nro_cliente)
INSERT INTO Plazo_cliente VALUES (1,2);
INSERT INTO Plazo_cliente VALUES (2,1);

# Prestamo (nro_prestamo AUTO_INCREMENT, fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente)
INSERT INTO Prestamo (fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES ("2020/02/01",2,12345, 32, 200, 1000, 1, 2);
INSERT INTO Prestamo (fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES ("2007/05/01",3,6789, 30, 100, 3000, 2, 1);

/*
# Pago (nro_prestamo, nro_pago, fecha_venc, fecha_pago)
INSERT INTO Pago VALUES (1,1,"2021/08/08",NULL);
INSERT INTO Pago VALUES (2,2,"2020/08/08",NULL);*/

# Tasa_prestamo (periodo, monto_inf, monto_sup, tasa)
INSERT INTO Tasa_prestamo VALUES (1,23000,29000,30); #24 
INSERT INTO Tasa_prestamo VALUES (3,5000,29000,10);
INSERT INTO Tasa_prestamo VALUES (5,27000,39000,40);
INSERT INTO Tasa_prestamo VALUES (1,2000,5000,50);


# Caja_ahorro (nro_ca AUTO_INCREMENT, CBU, saldo) 
INSERT INTO Caja_ahorro (CBU, saldo) VALUES (102030405060708090,200000.50);
INSERT INTO Caja_ahorro (CBU, saldo) VALUES (112030405060708091,300000.50);
INSERT INTO Caja_ahorro (CBU, saldo) VALUES (122030405060708092,5000.50);
INSERT INTO Caja_ahorro (CBU, saldo) VALUES (132030405060708093,1200000.50); #NADA DE PLATA

# Cliente CA (nro_cliente, nro_ca)
INSERT INTO Cliente_CA VALUES (1,1);
INSERT INTO Cliente_CA VALUES (2,2);
INSERT INTO Cliente_CA VALUES (3,3);
INSERT INTO Cliente_CA VALUES (4,4);

# Tarjeta (nro_tarjeta AUTO_INCREMENT, PIN, CVT, fecha_venc, nro_cliente, nro_ca) 
INSERT INTO Tarjeta (PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5("42091202"),md5("hola"),"2025/07/01",1,1);
INSERT INTO Tarjeta (PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5("42091900"),md5("diga"),"2028/08/03",2,2);
INSERT INTO Tarjeta (PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5("42094021"),md5("control"),"2029/12/11",3,3);
INSERT INTO Tarjeta (PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES (md5("42095090"),md5("reloj"),"2026/09/19",4,4);

# Caja (cod_caja AUTO_INCREMENT) NO SE PONE NADA
INSERT INTO Caja VALUES (); # asi? 
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();
INSERT INTO Caja VALUES ();

# Ventanilla (cod_caja, nro_suc)
# cod caja corresponde a una Caja y nro suc corresponde a una sucursal
INSERT INTO Ventanilla VALUES (1,1);
INSERT INTO Ventanilla VALUES (2,2);
#INSERT INTO Ventanilla VALUES (2,1);
#INSERT INTO Ventanilla VALUES (1,2);

# ATM (cod_caja, cod_postal, direccion)
INSERT INTO ATM VALUES (1,6409,"Avenida Siempre Viva 123");
INSERT INTO ATM VALUES (2,1850,"Alem 500");

# Transaccion (nro_trans AUTO_INCREMENT, fecha, hora, monto)
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/01/01","16:00",12000);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/01/01","16:02",13000);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/03/20","17:00",2000);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/10/11","16:11",100);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/12/22","06:01",1520);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/01/03","16:00",12900);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/03/25","17:00",22000);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/10/12","16:11",1100);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/10/22","06:11",11120);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/11/01","19:00",912000);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/03/20","21:00",82000);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/09/11","23:11",7100);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/01/22","08:01",5120);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/02/22","09:51",1620);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/05/11","23:11",7120);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/07/22","08:01",520);
INSERT INTO Transaccion (fecha, hora, monto) VALUES ("2020/06/22","09:51",120);

# Debito (nro_trans, descripcion, nro_cliente, nro_ca)
INSERT INTO Debito VALUES (1,"safaroni",1,1);
INSERT INTO Debito VALUES (2,"safa",2,2); 
INSERT INTO Debito VALUES (3,"re raro",3,3);
INSERT INTO Debito VALUES (4,"rarisimo",4,4);

# Transaccion_por_caja (nro_trans, cod_caja)
INSERT INTO Transaccion_por_caja VALUES (5,1);
INSERT INTO Transaccion_por_caja VALUES (6,2);
INSERT INTO Transaccion_por_caja VALUES (7,3);

INSERT INTO Transaccion_por_caja VALUES (8,1);
INSERT INTO Transaccion_por_caja VALUES (9,2);
INSERT INTO Transaccion_por_caja VALUES (10,3);

INSERT INTO Transaccion_por_caja VALUES (11,1);
INSERT INTO Transaccion_por_caja VALUES (12,2);
INSERT INTO Transaccion_por_caja VALUES (13,3);

# Deposito (nro_trans, nro_ca)
INSERT INTO Deposito VALUES (5,1);
INSERT INTO Deposito VALUES (6,2);
INSERT INTO Deposito VALUES (7,3);

# Extraccion (nro_trans, nro_cliente, nro_ca)
INSERT INTO Extraccion VALUES (8,1,1);
INSERT INTO Extraccion VALUES (9,2,2);
INSERT INTO Extraccion VALUES (10,3,3);

# Transferencia (nro_trans, nro_cliente, origen, destino)
INSERT INTO Transferencia VALUES (11,1,1,2);
INSERT INTO Transferencia VALUES (12,2,2,3);
INSERT INTO Transferencia VALUES (13,3,3,4);
