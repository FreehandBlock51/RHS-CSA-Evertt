package renderer;

import java.awt.Color;
import java.awt.image.WritableRaster;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import framework.Camera;
import framework.RayCastResult;
import framework.RenderObject;
import framework.RendererBase;
import framework.Vec3;
import framework.World;

// In this lab, you will write a basic ray-tracing renderer.
//  A framework has already been created for you. You will be writing a couple of
// methods put it all together. Your method, render, will write each pixel in an
// image, renderTarget, by casting a ray out from the camera, using rayCast, to 
// determine it hits. When it hits something in the scene/world, you'll get the
// color value at that point and then write it into the image.
// 
// -> Quick video, ray tracing basics: https://www.youtube.com/watch?v=oCsgTrGLDiI&t=6s
// -> Here's another, more detail: https://www.youtube.com/watch?v=gBPNO6ruevk 
// 
// Tip(s)...
//  - The image should be projected onto the "near clip plane", iterate through pointss
//      on that near clip rectangle (use camera to query info about it) and generate
//      rays from the camera's origin. Do this for all the pixels in that renderTarget.

public class Renderer implements RendererBase {

    /* rayCast  Determines THE FIRST ray intersection with the objects in the scene. 
     *            the ray is an infinite ray (infinite length starting at rayOrigin, 
     *            in the direction of rayDirUnit).
     *          The scene is composed of objects of type RenderObject. The full set of
     *            objects can be queried using: World.get().getRenderObjects()
     *          Each RenderObject supports its own rayCast method. They are all basic
     *            primitive types like planes and spheres. Their implementation does the
     *            math to perform the intersection with itself. You should make use 
     *            of this method in the implementation of this method.
     * 
     *  @param  world supports the interface to get the list of RenderObjects.
     *  @param  rayOrigin the start position of the ray
     *  @param  rayDirUnit the direction of the ray (should be a unit vector)
     *  @return the result of the first intersection along the ray (null if no intersection)
     *          see RayCastResult for details of its contents.
     */
    public RayCastResult rayCast(World world, Vec3 rayOrigin, Vec3 rayDirUnit) {
        RayCastResult res = null;
        double resDist = 0;

        for (RenderObject object : world.getRenderObjects()) {
            RayCastResult curResult;
            try {
                curResult = object.rayCast(rayOrigin, rayDirUnit);
            }
            catch (NullPointerException e) {
                curResult = null;
            }

            if (curResult == null) continue;
            if (res != null && resDist < curResult.pos.distance(rayOrigin)) continue;

            res = curResult;
            resDist = res.pos.distance(rayOrigin);
        }

        return res;
    }

    private static final double INTERSECT_EPSILON = 0.001;
    private static boolean areDoublesEqual(double a, double b) {
        return Math.abs(a - b) < INTERSECT_EPSILON;
    }
    private static boolean intersectsRay(Vec3 objPos, Vec3 rayStart, Vec3 rayDirUnit) {
        final double x = (objPos.x - rayStart.x) / rayDirUnit.x;
        final double y = (objPos.y - rayStart.y) / rayDirUnit.y;
        final double z = (objPos.z - rayStart.z) / rayDirUnit.z;
        return areDoublesEqual(x, y) && areDoublesEqual(y, z);
    }

    private static final double TAU = Math.PI * 2;
    private static final Random random = new Random();
    private static final int samples = 1;
    private static final double maxDeviation = 0;
    private static final double fieldDepth = 0;
    private static final int chunkSize = 1;

    // private int frameCount = 0;

    /* render   Performance ray trace rendering from the point of view of the provided camera.
     *          The method should iterate through each pixel in renderTarget, perform the
     *            expected ray tracing and write the resultant pixel color. 
     *          The provided camera object supports access to the camera position and orientation
     *            and in addition, methods to find the near clip plane (consider using this, hint hint).
     *          You should use the rayCast method that you implemented up above when ray tracing
     *            each pixel. Note that the RayCastResult object contains a resultant pixel color.
     * 
     *  @param  renderTarget is the image this method writes into. Use renderTarget.setPixel(x, y, color)
     *            so set individual pixels in the 2D coordinates of this image.
     *  @param  world supports the interface to get the list of RenderObjects.
     *  @param  camera is the camera objects that has a position, direction, field of view, etc.
     */
    public void render(WritableRaster renderTarget, World world, Camera camera) {
        final Vec3 cameraPos = camera.getPos();
        final Vec3 cameraLookDir = camera.forward();
        final Vec3 nearClipX = camera.getNearClipVecX();
        final Vec3 nearClipY = camera.getNearClipVecY();
        final Vec3 nearClipCenter = camera.getNearClipCenter();
        final int height = renderTarget.getHeight();
        final int width = renderTarget.getWidth();
        final int halfWidth = width / 2;
        final int halfHeight = height / 2;
        int[] colorArr = new int[] {0,0,0,255};

        for (int y = 0; y < renderTarget.getHeight(); y++) {
            for (int x = 0; x < renderTarget.getWidth(); x++) {
                if ((x % chunkSize == 0 || y % chunkSize == 0) || x * y == 0)
                {
                    final Vec3 offset = Vec3.add(
                        Vec3.multiply((double)(x - halfWidth) / width, nearClipX),
                        Vec3.multiply((double)(y - halfHeight) / height, nearClipY)
                    );
                    final Vec3 rayP2 = Vec3.add(nearClipCenter, offset);
                    final Vec3 rayP1 = cameraPos;
                    final Vec3 rayDirUnit = Vec3.subtract(rayP2, rayP1).unit();
                    rayP2.add(Vec3.multiply(fieldDepth, rayDirUnit));
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    int a = 0;
                    for (int i = 0; i < samples; i++) {
                        final Vec3 randOffset = Vec3.add(
                            Vec3.multiply(2 * maxDeviation * (random.nextDouble() - 0.5) / halfWidth, nearClipX.unit()),
                            Vec3.multiply(2 * maxDeviation * (random.nextDouble() - 0.5) / halfHeight, nearClipY.unit())
                        );
                        final Vec3 offsetP1 = Vec3.add(rayP1, randOffset);
                        final Vec3 offsetDirUnit = Vec3.subtract(rayP2, offsetP1).unit();
                        RayCastResult res = rayCast(world, offsetP1, offsetDirUnit);
                        if (res != null) {
                            final Color c = res.color;
                            r += c.getRed();
                            g += c.getGreen();
                            b += c.getBlue();
                            a += c.getAlpha();
                        }
                    }

                    colorArr[0] = r / samples;
                    colorArr[1] = g / samples;
                    colorArr[2] = b / samples;
                    colorArr[3] = a / samples;
                }
                renderTarget.setPixel(x, y, colorArr); // R, G, B, Alpha (0 to 255 values)
            }
        }
        // System.out.println("finished frame " + (++frameCount));
    }
}
