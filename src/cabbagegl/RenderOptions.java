package cabbagegl;
public class RenderOptions {

    public int width;
    public int height;
    
    public int AA_samples;

    public int max_recurse;

    public int dof_rays;
    public double focal_plane_dist;
    public double lens_aperture_radius;

    public RenderOptions() {
        width = 640;
        height = 640;
        AA_samples = 1;
        max_recurse = 10;


        dof_rays = 20;
        focal_plane_dist = 10;
        lens_aperture_radius = .35;
    }

    public RenderOptions(RenderOptions arg) {
        width = arg.width;
        height = arg.height;
        AA_samples = arg.AA_samples;
        max_recurse = arg.max_recurse;
        dof_rays = arg.dof_rays;
        focal_plane_dist = arg.focal_plane_dist;
        lens_aperture_radius = arg.lens_aperture_radius;
    }


    public String toString() {
        return "width: " + width +
        " height: " + height +
        " AA_samples: " + AA_samples +
        " max_recurse: " + max_recurse +
        " focal_plane_dist: " + focal_plane_dist +
        " lens_aperture_radius: " + lens_aperture_radius;
    }
}

