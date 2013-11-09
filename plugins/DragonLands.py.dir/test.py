

messages = [
    {
      "max": 45, 
      "string": "Even the youngling cephalopods know better how to navigate!", 
      "min": -45
    }, 
    {
      "max": 90, 
      "string": "If it was not for the powers of the Gates you would never find your home would you?", 
      "min": -90
    }, 
    {
      "max": 180, 
      "string": "You are so lost... You are looking AWAY from the hole in the world!", 
      "min": -180
    }, 
    {
      "max": 20, 
      "string": "Close enough, but if you want to be closer turn slightly", 
      "min": -20
    }
]
theta = -80
good_msgs=[]
for msg in messages:
    if msg['min'] < theta and msg['max'] > theta:
        #within base bounds...
        good_msgs.append(msg)
def delta_theta(msg):
    min_bound = abs(msg['min'] - theta)
    max_bound = abs(msg['max'] - theta)
    print "min_bound:%s,max_bound:%s,msg:%s"%(min_bound,max_bound,msg)
    return min((min_bound,max_bound))

print min(good_msgs,key=delta_theta)