public enum FaceSide {
   FRONT, BACK;

   public FaceSide not() {
      FaceSide ret = FRONT;
      if (this == FRONT)
         ret = BACK;
      return ret;
   }
}
