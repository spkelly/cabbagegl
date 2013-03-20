package cabbagegl;
public class RenderOptions {
    public RenderOptions() {
        width = 640;
        height = 640;
        AA_samples = 1;
        max_recurse = 10;

        depth_of_field = true;
        dist_to_focal_plane = 10;
        aperture_radius = .05;
        dof_samples = 10;
    }

    public int width;
    public int height;

    public int AA_samples;

    public int max_recurse;

    public boolean depth_of_field;
    public double dist_to_focal_plane;
    public double aperture_radius;
    public int dof_samples;
}

