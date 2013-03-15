from math import sqrt

class vec3:
   def __init__(self, ix, iy, iz):
      self.x = ix
      self.y = iy
      self.z = iz
   
   def sum(self, ov):
      x = self.x + ov.x
      y = self.y + ov.y
      z = self.z + ov.z
      return vec3(x,y,z)

   def __add__(self, ov):
      return self.sum(ov)

   def diff(self, ov):
      x = self.x - ov.x
      y = self.y - ov.y
      z = self.z - ov.z
      return vec3(x,y,z)

   def __sub__(self, ov):
      return self.diff(ov)

   def dot(self, ov):
      x = self.x * ov.x
      y = self.y * ov.y
      z = self.z * ov.z
      return x + y + z

   def cmul(self, ov):
      x = self.x * ov.x
      y = self.y * ov.y
      z = self.z * ov.z
      return vec3(x,y,z)
      
   def scale(self, s):
      x, y, z = self.x, self.y, self.z
      return vec3(x*s,y*s,z*s)
   
   def __mul__(self, other):
      ret = None
      if isinstance(other, int) or isinstance(other, float):
         ret = self.scale(other)
      elif isinstance(other, vec3):
         ret = self.cmul(vec3)
      return ret

   def length(self):
      x, y, z = self.x, self.y, self.z
      return sqrt(x*x + y*y + z*z)

   def __len__(self):
      return self.length();

   def normalize(self):
      l = len(self)
      return vec3(self.x/l, self.y/l, self.z/l)

   def __str__(self):
      x, y, z = self.x, self.y, self.z
      return "<%.3f, %.3f, %.3f>" % (x, y, z)

VZERO = vec3(0,0,0)
