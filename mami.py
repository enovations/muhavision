import pygame
from pygame import locals
import socket
import time
from time import sleep

pygame.init()

pygame.joystick.init()

try:
	j = pygame.joystick.Joystick(0)
	j.init()
except pygame.error:
	print 'no joystick found.'

UDP_IP = "127.0.0.1"
UDP_PORT1 = 1234
UDP_PORT2 = 2345
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

loop = 1000 / 20
updates = 0
delta = 0

while 1:
	start_time = int(round(time.time() * 1000))

	for e in pygame.event.get(): # iterate over event stack
		if e.type == pygame.locals.JOYAXISMOTION: # 7
			x, y, z, a = (j.get_axis(0)+0.01)*100, (j.get_axis(1)+0.01)*100, (j.get_axis(2)+0.01)*100, (j.get_axis(3)+0.1)*2
			MESSAGE = str(int(x)) + ';' + str(int(y)) + ';' + str(int(z)) + ';' + str(int(a)) + '\n\r'
			sock.sendto(MESSAGE, (UDP_IP, UDP_PORT1))
		if e.type == pygame.locals.JOYBUTTONDOWN: # 10
			a, b, c, d, e, f, g, h, i, v, k, l = j.get_button(0), j.get_button(1), j.get_button(2), j.get_button(3), j.get_button(4), j.get_button(5), j.get_button(6), j.get_button(7), j.get_button(8), j.get_button(9), j.get_button(10), j.get_button(11)
			MESSAGE = str(a) + ';' + str(b) + ';' + str(c) + ';' + str(d) + ';' + str(e) + ';' + str(f) + ';' + str(g) + ';' + str(h) + ';' + str(i) + ';' + str(v) + ';' + str(k) + ';' + str(l) + '\n\r'
			sock.sendto(MESSAGE, (UDP_IP, UDP_PORT2))

	end_time = int(round(time.time()*1000))
	difference = end_time - start_time
	if difference > 0:
		sleep((loop - difference) / 1000.0)
		updates = updates + 1
		delta = delta + difference
		if updates == 20:
			updates = 0