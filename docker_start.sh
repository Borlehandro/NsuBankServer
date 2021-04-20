# TODO Add maven install here
sudo docker build -t bank-server .
sudo docker run --rm --name=bank-server --net=bank_network -p 8080:8080 bank-server