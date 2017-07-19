import matplotlib.pyplot as plt
import matplotlib.animation as animation
import orbitSS


plt.axes(xlim=(-4,4), ylim=(-4,4))
plt.gca().set_aspect('equal')

fig = plt.gcf()
ax=fig.gca()

sun = plt.Circle((0,0),.3,color='yellow')
ax.add_artist(sun)

def make_SS_obj(orbitRadius, objRadius, objColor):
    orbit = orbitSS.getEpiCycle(0, 0, orbitRadius)
    objX, objY = orbitSS.getEpiPoint(0, 0, 0, orbitRadius, 0)
    obj = plt.Circle((objX, objY), objRadius, color = objColor)
    return orbit, obj

mercuryOrbit, mercury = make_SS_obj(.6, .1, 'brown')
venusOrbit, venus = make_SS_obj(1, .15, 'orange')
earthOrbit, earth = make_SS_obj(1.6, .15, 'blue')

earthX, earthY = earth.center
moonEpicycle = orbitSS.getEpiCycle(earthX, earthY, .3)
moonX, moonY = orbitSS.getEpiPoint(0, earthX, earthY, .3, 0)
moon = plt.Circle((moonX, moonY), .1, color='gray')

marsOrbit, mars = make_SS_obj(2.2, .12, 'red')
jupiterOrbit, jupiter = make_SS_obj(2.6, .2, 'brown')
saturnOrbit, saturn = make_SS_obj(3.2, .2, 'orange')

def obj_init(obj, orbit):
    ax.add_patch(orbit)
    ax.add_patch(obj)

def init():
    obj_init(mercury, mercuryOrbit)
    obj_init(venus, venusOrbit)
    obj_init(earth, earthOrbit)
    obj_init(moon, moonEpicycle)
    obj_init(mars, marsOrbit)
    obj_init(jupiter, jupiterOrbit)
    obj_init(saturn, saturnOrbit)
    return mercury, venus, earth, moon, moonEpicycle, mars, jupiter, saturn,

def moon_anim(i):
    moonX, moonY = earth.center
    moonEpicycle.center = (moonX, moonY)
    moon.center = orbitSS.getEpiPoint(i, moonX, moonY, moonEpicycle.get_radius(), 12)

def obj_anim(i, obj, orbit, increment):
    objX, objY = orbitSS.getEpiPoint(i, 0, 0, orbit.get_radius(), increment)
    obj.center = (objX, objY)

# can continue animating by calling animate(i) iteratively for however many frames
# the planet will move around its orbit by replacing its center on each point of the orbit
def animate(i):
    obj_anim(i, mercury, mercuryOrbit, 1.8)
    obj_anim(i, venus, venusOrbit, 1.4)
    obj_anim(i, earth, earthOrbit, 1)
    moon_anim(i)
    obj_anim(i, mars, marsOrbit, .8)
    obj_anim(i, jupiter, jupiterOrbit, .6)
    obj_anim(i, saturn, saturnOrbit, .4)
    return mercury, venus, earth, moon, moonEpicycle, mars, jupiter, saturn

# create the actual animation object and save it as an mp4
# saves animation.mp4 in the working directory
# add more frames for longer animation, less for shorter
anim = animation.FuncAnimation(fig, animate, init_func=init, frames = 1000)
anim.save('copernican.mp4',fps=30,extra_args=['-vcodec','h264','-pix_fmt','yuv420p'])
