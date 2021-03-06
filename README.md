# creatingo the container
docker build -t vrios/softplan_test:v1 .

# run on port 8081
docker run --name softplan_test -p 8081:8081 vrios/softplan_test:v1

# 
docker tag vrios/softplan_test:v1 vrios/softplan_test:v1

docker create --name softplan_test -p 8081:8081 vrios/softplan_test:v1

docker start softplan_test

docker commit softplan_test vrios/softplan_test:v1

docker push vrios/softplan_test:v1

