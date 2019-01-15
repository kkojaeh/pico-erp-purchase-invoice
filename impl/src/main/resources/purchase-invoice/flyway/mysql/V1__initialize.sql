create table pci_purchase_invoice (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	due_date datetime,
	invoice_id binary(16),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	purchase_order_id binary(16),
	remark varchar(50),
	status varchar(20),
	primary key (id)
) engine=InnoDB;

create table pci_purchase_invoice_item (
	id binary(16) not null,
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	invoice_id binary(16),
	invoice_item_id binary(16),
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	order_item_id binary(16),
	quantity decimal(19,2),
	remark varchar(50),
	primary key (id)
) engine=InnoDB;

alter table pci_purchase_invoice
	add constraint UK6hajqb6rmwhufpp6uvd5jnv7x unique (invoice_id);

create index IDXss0g1pwhc4ud45fym1kak4byo
	on pci_purchase_invoice_item (invoice_id);
