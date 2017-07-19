import matplotlib.pyplot as plt
import math

def getEpiCycle(centerX, centerY, radius):
    return plt.Circle((centerX, centerY), radius, color='black', fill=False)

def getEpiPoint(i, centerX, centerY, radius, factor):
    radAngle = math.radians((i * factor) % 360)
    # do 360 - ((i * factor) % 360) to go clockwise. untested
    # adjust the factor that you pass in to change the speed of orbit
    x = (radius * math.cos(radAngle)) + centerX
    y = (radius * math.sin(radAngle)) + centerY
    return x,y


