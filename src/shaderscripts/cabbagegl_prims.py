from vec3 import *

class shape:
   def __init__(self):
      self.material = material()

class material:
   def __init__(self):
      self.ambient = VZERO
      self.diffuse = VZERO
      self.specular = VZERO
      self.position = VZERO
      self.shininess = 2.0

   def __str__(self):
      amb = str(self.ambient)
      dif = str(self.diffuse)
      spc = str(self.specular)
      shi = self.shininess
      return "material properties:\n" +\
         "ambient: %s\ndiffuse: %s\nspecular: %s\nshine: %f\n" % \
         (amb, dif, spc, shi)

class fragment:
   def __init__(self):
      self.material = material()
      self.position = VZERO
      self.normal = VZERO

      self.distTo = 0.0
      self.isFront = True

   def __str__(self):
      mat = str(self.material)
      pos = str(self.position)
      nrm = str(self.normal)

      dto = self.distTo
      sde = "BACK"
      if self.isFront:
         sde = "FRONT"
      return ("%s\nfragment properties:\nposition: %s\nnormal: %s\nside: %s\n" +\
         "dist from ray fired: %.3f") % (mat, pos, nrm, sde, dto)
      

