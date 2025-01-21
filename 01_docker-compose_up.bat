cd ./
docker-compose build
docker-compose up -d
copy .\\.env .\\server\\src\main\resources\\db.properties
pause

