
CREATE TABLE `farmers` (
	`name` varchar(255) NOT NULL,
	`address` varchar(255) NOT NULL,
	`id` int NOT NULL AUTO_INCREMENT,
	`rating` smallint NOT NULL DEFAULT '0',
	`phonenumber` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `customers` (
	`name` varchar(255) NOT NULL,
	`id` int NOT NULL AUTO_INCREMENT,
	`phonenumber` varchar(255) NOT NULL,
	`address` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `collectionpoints` (
	`name` varchar(255) NOT NULL,
	`name` varchar(255) NOT NULL,
	`id` int NOT NULL AUTO_INCREMENT,
	`address` varchar(255) NOT NULL,
	`commision` FLOAT NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
	`username` varchar(255) NOT NULL,
	`role` varchar(255) NOT NULL,
	`userid` int NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (`userid`)
);

CREATE TABLE `orders` (
	`customerid` int NOT NULL,
	`productid` int NOT NULL,
	`collectionpointid` int NOT NULL,
	`orderstatus` enum NOT NULL
);

CREATE TABLE `products` (
	`farmerid` int NOT NULL,
	`productname` varchar(255) NOT NULL,
	`id` int NOT NULL AUTO_INCREMENT,
	`price` int NOT NULL,
	`quantity` int NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `loggedin` (
	`username` varchar(255) NOT NULL,
	`userid` int NOT NULL
);

ALTER TABLE `orders` ADD CONSTRAINT `orders_fk0` FOREIGN KEY (`customerid`) REFERENCES `customers`(`id`);

ALTER TABLE `orders` ADD CONSTRAINT `orders_fk1` FOREIGN KEY (`productid`) REFERENCES `products`(`id`);

ALTER TABLE `orders` ADD CONSTRAINT `orders_fk2` FOREIGN KEY (`collectionpointid`) REFERENCES `collectionpoints`(`id`);

ALTER TABLE `products` ADD CONSTRAINT `products_fk0` FOREIGN KEY (`farmerid`) REFERENCES `farmers`(`id`);

ALTER TABLE `loggedin` ADD CONSTRAINT `loggedin_fk0` FOREIGN KEY (`username`) REFERENCES `users`(`username`);

ALTER TABLE `loggedin` ADD CONSTRAINT `loggedin_fk1` FOREIGN KEY (`userid`) REFERENCES `users`(`userid`);








