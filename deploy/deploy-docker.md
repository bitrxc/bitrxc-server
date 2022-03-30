# How to deploy this client with docker

## Config and Deploy

Open shell(bash or sh), make sure current working directory is at root of this repo, usually named as `bitrxc-admin-client`. 

Prepare `application.yml` file. First cppy example file into given destination, see command below:
```bash
cp ./deploy/application.example.yaml ./service/src/main/resources/application.yaml
```

Then edit copied file, make sure that fields about WeChat OpenPlatform is set to valid value other than example vale below:
```yaml
wx:
  appId: wx123456789abcdef0
  secret: 123456789abcdef0123456789abcdef0
```

Then execute commands below to build and deploy product server:
```bash
docker-compose --env-file ./deploy/environment/.env down
docker-compose --env-file ./deploy/environment/.env up --detach --build
```

## Interact with database

Run with another environment variable file to launch another instance of this docker image.

Execute following command to login to database on the fly. Make sure that `<mariadb-container>` is replaced into container name defined in env file. For example, if `deploy/environment/.env.dev` is used, `<mariadb-container>` should be replaced by `mariadb-dev`
```bash
docker exec -it <mariadb-container> mariadb -uroot -proot --database bitrxc
docker exec -t <mariadb-container> mariadb -uroot -proot --database bitrxc < backup.sql
```

Further infomation about mariadb image locates [on docker hub](https://hub.docker.com/_/mariadb/).