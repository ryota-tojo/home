FROM php:8.0-apache

RUN apt-get update && apt-get -y install libpq-dev
RUN docker-php-ext-install pdo_pgsql

RUN apt-get update
RUN apt-get -y install libzip-dev
RUN docker-php-ext-install zip

COPY php.ini /usr/local/etc/php/