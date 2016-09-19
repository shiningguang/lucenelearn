create table t_attachment(
	id int(10) PRIMARY KEY AUTO_INCREMENT,
	new_name varchar(100) CHARACTER SET utf8,
	old_name varchar(100) CHARACTER SET utf8,
	content_type varchar(100) CHARACTER SET utf8,
	create_date datetime,
	msg_id int(10),
	CONSTRAINT FOREIGN KEY(msg_id) REFERENCES t_message(id)
)