package cabbagegl;
public class RenderOptions {
    public RenderOptions() {
        width = 640;
        height = 640;
        AA_samples = 1;
        max_recurse = 10;


        dof_rays = 20;
        focal_plane_dist = 10;
        lens_aperture_radius = .35;
    }

    public int width;
    public int height;

    public int AA_samples;

    public int max_recurse;

    public int dof_rays;
    public double focal_plane_dist;
    public double lens_aperture_radius;
}

