package app;

import framework.CameraBase;
import framework.Window;
import framework.WorldBase;
import math.Mat3x3;
import math.Vec2;
import math.Vec3;

/* In this lab, you will be implementing a camera that lets you fly/run around the
 *   world. Before any modifications, you'll just be stuck looking forward and unable
 *   to move. All of your code can go in this file.
 * 
 * Inputs...
 *   - Arrow keys (including WASD) cab be queried with the methods...
 *          isArrowKeyPressed_Left(), isArrowKeyPressed_Right(),
 *          isArrowKeyPressed_Up(), isArrowKeyPressed_Down()
 *   - Mouse position can be queried with...
 *          getMouseCursorPos()
 * 
 * You would make the motion nice and smooth (using acceleration & velocity for position).
 *  You can get and set the position of the camera in 3D space with getPos() and setPos(...).
 *  The mouse look is a bit tricker. The orientation of the camera is specified by two 
 *  vectors: lookDirection (use getLookDir() & setLookDir(...) for access) and 
 *  upVector (use up() & setUpDir(...) for access). You can use the Mat3x3 class to help
 *  you with rotation about an axis. For example, the static Mat3x3.transRotAxis(...) method
 *  creates a 3x3 rotation matrix about the specified axis. You can use this to rotate vectors.
 *  I'd suggest watching a youTube video about rotating vectors with 3x3 matrices to get
 *  you started.
 */
public class Camera extends CameraBase {
    // consts
    private static final double MOVE_SPEED = 30;
    private static final double LOOK_SPEED = 50;

    // data
    private Vec2 prevMouseCursorPos;

    // constructor(s)
    public Camera(Vec3 pos, Vec3 forward) {
        super(pos, forward, Vec3.up(), 80, 0.1, 1000.0);
        prevMouseCursorPos = getMouseCursorPos();
    }

    // methods
    private void updateMotion(double deltaTime) {
        // World access
        WorldBase world = WorldBase.get();

        Vec3 velocity = new Vec3(0, 0, 0);
        if (isArrowKeyPressed_Down()) {
            velocity.add(Vec3.multiply(backward(), MOVE_SPEED));
        }
        if (isArrowKeyPressed_Up()) {
            velocity.add(Vec3.multiply(forward(), MOVE_SPEED));
        }
        if (isArrowKeyPressed_Left()) {
            velocity.add(Vec3.multiply(left(), MOVE_SPEED));
        }
        if (isArrowKeyPressed_Right()) {
            velocity.add(Vec3.multiply(right(), MOVE_SPEED));
        }
        setPos(Vec3.add(getPos(), Vec3.multiply(velocity, deltaTime)));
    }
    private static final double TAU = Math.PI * 2;
    private void updateLookDirection(double deltaTime) {
        final Window window = Window.get();
        final Vec2 curMouseCursorPos = getMouseCursorPos();
        final Vec2 mouseMovement = Vec2.subtract(curMouseCursorPos, prevMouseCursorPos);

        final Vec2 rotation = new Vec2(mouseMovement.x * TAU / window.getWidth(), mouseMovement.y * TAU / window.getHeight());
        rotation.multiply(Math.PI * 2);
        Vec3 curLookDir = getLookDir();
        
        final Mat3x3 xRotMat = Mat3x3.transRotAxis(Vec3.up(), rotation.x * LOOK_SPEED * deltaTime);
        final Mat3x3 yRotMat = Mat3x3.transRotAxis(left(), -rotation.y * LOOK_SPEED * deltaTime);
        setLookDir(
            xRotMat.transform(
                yRotMat.transform(
                    curLookDir
                )
            )
            .unit()
        );
        setUpDir(
            xRotMat.transform(
                yRotMat.transform(
                    up()
                )
            )
            .unit()
        );

        prevMouseCursorPos = curMouseCursorPos;
    }
    public void update(double deltaTime) {
        updateMotion(deltaTime);
        updateLookDirection(deltaTime);
    }
}
