import matplotlib.pyplot as plt
import matplotlib.animation as animation
import orbitSS


plt.axes(xlim=(-8,8), ylim=(-8,8))
plt.gca().set_aspect('equal')

fig = plt.gcf()
ax=fig.gca()

earth = plt.Circle((0,0),.15,color='blue')
ax.add_artist(earth)

def make_SS_obj(orbitRadius, epicycleRadius, objRadius, objColor):
    orbit = orbitSS.getEpiCycle(0, 0, orbitRadius)
    epiX, epiY = orbitSS.getEpiPoint(0, 0, 0, orbitRadius, 0)
    epicycle = orbitSS.getEpiCycle(epiX, epiY, epicycleRadius)
    objX, objY = orbitSS.getEpiPoint(0, 0, 0, epicycleRadius, 0)
    obj = plt.Circle((objX, objY), objRadius, color = objColor)
    return orbit, epicycle, obj

moonOrbit, moonEpicycle, moon = make_SS_obj(.6, .2, .1, 'gray')
mercuryOrbit, mercuryEpicycle, mercury = make_SS_obj(1.4, .2, .1, 'brown')
venusOrbit, venusEpicycle, venus = make_SS_obj(2.2, .2, .15, 'orange')

sunOrbit = orbitSS.getEpiCycle(0, 0, 3.2)
sunX, sunY = orbitSS.getEpiPoint(0, 0, 0, 3.2, 0)
sun = plt.Circle((sunX, sunY), .3, color='yellow')

marsOrbit, marsEpicycle, mars = make_SS_obj(4, .3, .12, 'red')
jupiterOrbit, jupiterEpicycle, jupiter = make_SS_obj(5.2, .4, .2, 'brown')
saturnOrbit, saturnEpicycle, saturn = make_SS_obj(6.4, .4, .2, 'orange')
saturnTrace = plt.Circle((0,0), .01, color = 'black')

def obj_init(obj, orbit, epicycle, trace):
    if trace is not None:
        ax.add_artist(trace)
    ax.add_patch(orbit)
    ax.add_patch(epicycle)
    ax.add_patch(obj)

def sun_init():
    ax.add_patch(sunOrbit)
    ax.add_patch(sun)

def init():
    
    sunX, sunY = sun.center
    earthX, earthY = earth.center
    x = [earthX, sunX]
    y = [earthY, sunY]
    rod = plt.plot(x, y, 'black')
    obj_init(moon, moonOrbit, moonEpicycle, None)
    obj_init(mercury, mercuryOrbit, mercuryEpicycle, None)
    obj_init(venus, venusOrbit, venusEpicycle, None)
    sun_init()
    obj_init(mars, marsOrbit, marsEpicycle, None)
    obj_init(jupiter, jupiterOrbit, jupiterEpicycle, None)
    obj_init(saturn, saturnOrbit, saturnEpicycle, saturnTrace)
    return moon, moonEpicycle, mercury, mercuryEpicycle, venus, venusEpicycle, sun, mars, marsEpicycle, jupiter, jupiterEpicycle, saturn, saturnEpicycle

def obj_anim(i, obj, orbit, epicycle, factor, trace):
    objEpiX, objEpiY = orbitSS.getEpiPoint(i, 0, 0, orbit.get_radius(), factor)
    epicycle.center = (objEpiX, objEpiY)
    obj.center = orbitSS.getEpiPoint(i, objEpiX, objEpiY, epicycle.get_radius(), 10)
    if trace != None:
        trace = plt.Circle(obj.center, .02, color = 'black')
        ax.add_artist(trace)
    # if they need to go around their epicycles at different speeds,
    # accept a 6th argument factor2 and use that as the last argument for getEpiPoint in the last line

def sun_anim(i):
    sunX, sunY = orbitSS.getEpiPoint(i, 0, 0, sunOrbit.get_radius(), 2)
    sun.center = (sunX, sunY)

# can continue animating by calling animate(i) iteratively for however many frames
# the planet will move around its orbit by replacing its center on each point of the orbit
def animate(i):
    ax.lines.remove(ax.lines[0])
    sun_anim(i)
    sunX, sunY = sun.center
    earthX, earthY = earth.center
    x = [earthX, sunX]
    y = [earthY, sunY]
    plt.plot(x, y, 'black')
    obj_anim(i, moon, moonOrbit, moonEpicycle, 3, None)
    obj_anim(i, mercury, mercuryOrbit, mercuryEpicycle, 2, None)
    obj_anim(i, venus, venusOrbit, venusEpicycle, 2, None)
    obj_anim(i, mars, marsOrbit, marsEpicycle, .8, None)
    obj_anim(i, jupiter, jupiterOrbit, jupiterEpicycle, .6, None)
    obj_anim(i, saturn, saturnOrbit, saturnEpicycle, .4, saturnTrace)
    return moon, moonEpicycle, mercury, mercuryEpicycle, venus, venusEpicycle, sun, mars, marsEpicycle, jupiter, jupiterEpicycle, saturn, saturnEpicycle

# create the actual animation object and save it as an mp4
# saves animation.mp4 in the working directory
# add more frames for longer animation, less for shorter
anim = animation.FuncAnimation(fig, animate, init_func=init, frames = 1000)
anim.save('ptolemaic.mp4',fps=30,extra_args=['-vcodec','h264','-pix_fmt','yuv420p'])
