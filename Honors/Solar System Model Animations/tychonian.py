import matplotlib.pyplot as plt
import matplotlib.animation as animation
import orbitSS


plt.axes(xlim=(-7,7), ylim=(-7,7))
plt.gca().set_aspect('equal')

fig = plt.gcf()
ax=fig.gca()

earth = plt.Circle((0,0),.15,color='blue')
ax.add_artist(earth)

moonOrbit = orbitSS.getEpiCycle(0, 0, .5)
moonX, moonY = orbitSS.getEpiPoint(0, 0, 0, .3, 0)
moon = plt.Circle((moonX, moonY), .1, color='gray')

sunOrbit = orbitSS.getEpiCycle(0, 0, 2)
sunX, sunY = orbitSS.getEpiPoint(0, 0, 0, 1, 0)
sun = plt.Circle((sunX, sunY), .3, color='yellow')

mercury = plt.Circle((sunX, sunY),.1,color='brown')
mercuryEpicycle = orbitSS.getEpiCycle(sunX, sunY, .6)

venus = plt.Circle((sunX, sunY),.15,color='orange')
venusEpicycle = orbitSS.getEpiCycle(sunX, sunY, 1.2)

mars = plt.Circle((sunX,sunY),.12,color='red')
marsEpicycle = orbitSS.getEpiCycle(sunX,sunY, 2.7)

jupiter = plt.Circle((sunX,sunY),.2,color='brown')
jupiterEpicycle = orbitSS.getEpiCycle(sunX,sunY, 3.3)

saturn = plt.Circle((sunX,sunY),.2,color='orange')
saturnEpicycle = orbitSS.getEpiCycle(sunX,sunY, 3.9)

def planet_init(planet, epicycle):
    planet.center = orbitSS.getEpiPoint(0, sunX, sunY, epicycle.get_radius(), 0)
    ax.add_patch(epicycle)
    ax.add_patch(planet)

def obj_init(obj, orbit):
    ax.add_patch(orbit)
    ax.add_patch(obj)

def init():
    obj_init(moon, moonOrbit)
    obj_init(sun, sunOrbit)
    planet_init(mercury, mercuryEpicycle)
    planet_init(venus, venusEpicycle)
    planet_init(mars, marsEpicycle)
    planet_init(jupiter, jupiterEpicycle)
    planet_init(saturn, saturnEpicycle)
    return moon, sun, mercury, mercuryEpicycle, venus, venusEpicycle, mars, marsEpicycle, jupiter, jupiterEpicycle, saturn, saturnEpicycle

def planet_anim(i, planet, epicycle, increment):
    sunX, sunY = sun.center
    epicycle.center = (sunX, sunY)
    planet.center = orbitSS.getEpiPoint(i, sunX, sunY, epicycle.get_radius(), increment)

def obj_anim(i, obj, orbit, increment):
    objX, objY = orbitSS.getEpiPoint(i, 0, 0, orbit.get_radius(), increment)
    obj.center = (objX, objY)

# can continue animating by calling animate(i) iteratively for however many frames
# the planet will move around its orbit by replacing its center on each point of the orbit
def animate(i):
    obj_anim(i, moon, moonOrbit, 5)
    obj_anim(i, sun, sunOrbit, .417)
    planet_anim(i, mercury, mercuryEpicycle, 2)
    planet_anim(i, venus, venusEpicycle, 1.8)
    planet_anim(i, mars, marsEpicycle, .8)
    planet_anim(i, jupiter, jupiterEpicycle, .6)
    planet_anim(i, saturn, saturnEpicycle, .4)
    return moon, sun, mercury, mercuryEpicycle, venus, venusEpicycle, mars, marsEpicycle, jupiter, jupiterEpicycle, saturn, saturnEpicycle

# create the actual animation object and save it as an mp4
# saves animation.mp4 in the working directory
anim = animation.FuncAnimation(fig, animate, init_func=init, frames = 1000)
anim.save('tychonian.mp4',fps=30,extra_args=['-vcodec','h264','-pix_fmt','yuv420p'])
