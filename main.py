import tkinter as tk
from tkinter import font

import io
import json
import time
from PIL import Image, ImageTk
import requests
from io import BytesIO
import os.path
from os import path
from decimal import Decimal
from datetime import datetime

directory = "fpa2 any"
animation_duration = int(1400/7)

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKCYAN = '\033[96m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


lastClickX = 0
lastClickY = 0


def SaveLastClickPos(event):
    global lastClickX, lastClickY
    lastClickX = event.x
    lastClickY = event.y


def Dragging(event):
    x, y = event.x - lastClickX + root.winfo_x(), event.y - lastClickY + root.winfo_y()
    root.geometry("+%s+%s" % (x , y))

def fit_to_pixels(text,pixels,max_size):
    testFont = tk.font.Font(family='Roboto Black')
    size = max_size
    testFont.config(size=size)
    while(testFont.measure(text) > pixels and size > 1):
        size -= 1
        testFont.config(size=size)
    return size

def make_text(y,text,pixels,color,max_size=150):
    return c.create_text(285,y,anchor="center",font=("Roboto Black",fit_to_pixels(text,pixels,max_size)),fill=color,text=text)

def hex_to_rgb(value):
    value = value.lstrip('#')
    lv = len(value)
    return tuple(int(value[i:i+lv//3], 16) for i in range(0, lv, lv//3))

def get_image(img_url):
    response = requests.get(img_url)
    img = Image.open(BytesIO(response.content))
    wid, hgt = img.size
    if wid != hgt:
        print(f"{bcolors.WARNING}Warning: image from url {img_url} is not square.{bcolors.ENDC}")
    if wid < 300 or hgt < 300:
        print(f"{bcolors.WARNING}Warning: image from url {img_url} is low resolution.{bcolors.ENDC}")    
    img = img.resize((500, 500), Image.ANTIALIAS)
    return img

def check_resize(img):
    wid, hgt = img.size
    if wid != 500 or hgt != 500:
        img = img.resize((500, 500), Image.ANTIALIAS)
        img.save(directory + "/" + player['name'] + ".png")
        

def time_convert(s):
    s = Decimal(s)
    min, sec = divmod(s, 60) 
    hour, min = divmod(min, 60) 
    decimal = "{:.3f}".format(s)
    decimal = decimal[-4:] 
    if decimal == ".000":
        decimal = ""
    time = "%d:%02d:%02d" % (hour, min, sec) + decimal
    while time[0] == "0" or time[0] == ":":
        time = time[1:]
    return time

def date_convert(d):
    date = datetime.strptime(d, '%Y-%m-%d').strftime("%m/%d/%Y")
    after_slash = date.index("/") +1
    if date[after_slash] == "0":
        date = date[0:after_slash] + date[after_slash+1:]
    if date[0] == "0":
        date = date[1:]
    return date

def update_loop():
    root.after(animation_duration,update_loop)
    with open(directory + '/data.txt') as json_file:
        data = json.load(json_file)
        if c.count < c.stages:
            record = data['records'][c.count]

            new_player = record['player']
            if c.player != new_player:
                c.player = new_player
                c.player_color = record['player_color']
                c.image = ImageTk.PhotoImage(Image.open(directory + "/" + c.player + ".png"))
                c.itemconfig(c.WRHolderName,font=("Roboto Black",fit_to_pixels(c.player,500,90)),text=c.player,fill=c.player_color)
                c.itemconfig(c.WRHolderImage, image=c.image)

            new_time = time_convert(record['time'])
            if c.time != new_time:
                c.time = new_time
                c.itemconfig(c.WRTime,font=("Roboto Black",fit_to_pixels(c.time,500,140)),text=c.time)
            
            new_date = date_convert(record['date'])
            if c.date != new_date:
                c.date = new_date
                c.itemconfig(c.WRDate,font=("Roboto Black",fit_to_pixels(c.date,500,70)),text=c.date)

            c.count += 1
            
    

root = tk.Tk()
root.overrideredirect(True)
root.geometry("570x1080+1350+0")
root.attributes('-topmost', True)
root.bind('<Button-1>', SaveLastClickPos)
root.bind('<B1-Motion>', Dragging)

c = tk.Canvas(root,bg="black",height="1080",width="570",highlightthickness=0)
c.WRHolderText = make_text(75,"WR Holder:", 520, "white")
c.WRHolderName = c.create_text(285,715,anchor="center") #make_text(715,"Loading...", 475, "white",100)
c.WRTime = c.create_text(285,860,anchor="center",fill="white") #make_text(860,"Loading...", 500, "white",140)
c.WRDate = c.create_text(285,1000,anchor="center",fill="white") #make_text(1000,"Loading...", 450, "white")
c.WRHolderImage = c.create_image(285, 400, anchor='center')
c.count = 0

with open(directory + '/data.txt') as json_file:
    data = json.load(json_file)
    c.stages = len(data['records']) #set stage number
    for player in data['players']:
        #deal with images
        if not path.exists(directory + "/" + player['name'] + ".png"):
            img_url = player['image']
            if img_url == None:
                print(f"{bcolors.WARNING}Warning: {player['name']} has no associated image url.{bcolors.ENDC}")
                img = Image.new(mode= 'RGB', size = (500, 500), color = hex_to_rgb(player['color']))
            else:
                img = get_image(img_url) 
            img = img.save(directory + "/" + player['name'] + ".png")
        check_resize(Image.open(directory + "/" + player['name'] + ".png"))

c.player = None
c.player_color = None
c.time = None
c.date = None
c.image = None
c.pack()
root.after(0,update_loop)
root.mainloop()

