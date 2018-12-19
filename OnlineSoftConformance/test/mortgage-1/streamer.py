import socket
import time

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = "127.0.0.1"
port = 1234
serversocket.bind((host, port))
serversocket.listen(5)

while 1:
	(clientsocket, address) = serversocket.accept()
	filepath = 'log.stream.csv'
	with open(filepath) as fp:  
		line = fp.readline()
		while line:
			clientsocket.send(line.strip().encode() + "\n") 
			line = fp.readline()
			time.sleep(0.01)
