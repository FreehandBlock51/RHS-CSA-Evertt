package framework;

import java.awt.Color;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL;

import math.Mat3x3;
import math.Mat4x4;
import math.Vec2;
import math.Vec3;
import math.Vec4;

public class Util {
    // Math utility functions...
    public static double clamp(double x, double min, double max) {
        return Math.max(min, Math.min(x, max));
    }
    public static double lerp(double a, double b, double t) {
        return (a + (b - a)* t);
    }
    public static double sinDeg(double x) {
        return Math.sin(Math.toRadians(x));
    }
    public static double cosDeg(double x) {
        return Math.cos(Math.toRadians(x));
    }
    public static double tanDeg(double x) {
        return Math.tan(Math.toRadians(x));
    }
    public static double asinDeg(double x) {
        return Math.toDegrees(Math.asin(x));
    }
    public static double acosDeg(double x) {
        return Math.toDegrees(Math.acos(x));
    }
    public static double atanDeg(double x) {
        return Math.toDegrees(Math.atan(x));
    }
    public static double atan2Deg(double y, double x) {
        double angle = Math.toDegrees(Math.atan2(y, x));
        return (angle < 0) ? angle + 360 : angle;
    }
    public static double minAngleToAngleDelta(double a1, double a2) {
        double d1 = a2 - a1;
        double d2 = a2 - (a1 + 360);
        double d3 = a2 - (a1 - 360);
        if (Math.abs(d1) < Math.abs(d2)) {
            return (Math.abs(d1) < Math.abs(d3)) ? d1 : d3;
        }
        return (Math.abs(d2) < Math.abs(d3)) ? d2 : d3;
    }

    // Misc utils...
    public static double randRange(double min, double max) {
        return min + Math.random() * (max - min);
    }
    public static String toIntStringCeil(double value) {
        int valueInt = (int)Math.ceil(value);
        return String.valueOf(valueInt);
    }
    public static String toIntStringFloor(double value) {
        int valueInt = (int)Math.floor(value);
        return String.valueOf(valueInt);
    }

    // Color utils...
    public static Color colorLerp(Color colorA, Color colorB, double t) {
        return colorLerp(colorA, colorB, (float)t);
    }
    public static Color colorLerp(Color colorA, Color colorB, float t) {
        if (t <= 0) {
            return colorA;
        }
        else if (t >= 1) {
            return colorB;
        }
        return new Color(
            (int)lerp((float)colorA.getRed(), (float)colorB.getRed(), t),
            (int)lerp((float)colorA.getGreen(), (float)colorB.getGreen(), t),
            (int)lerp((float)colorA.getBlue(), (float)colorB.getBlue(), t));
    }

    // Override the log...
    public static void log(String str) {
        System.out.println(str);
    }

    // Intersection utils...
    public static boolean circlesIntersect(Vec2 p1, double r1, Vec2 p2, double r2) {
        double dstSqr = Vec2.distanceSqr(p1, p2);
        double radSum = r1 + r2;
        return (dstSqr < radSum * radSum);
    }
    public static boolean circleContainsPoint(Vec2 circlePos, double circleRad, Vec2 point) {
        double dstSqr = Vec2.distanceSqr(circlePos, point);
        return (dstSqr < circleRad * circleRad);
    }
    public static Vec2 intersectSegments(Vec2 seg0start, Vec2 seg0dir, Vec2 seg1start, Vec2 seg1dir) {
        double s = (-seg0dir.y * (seg0start.x - seg1start.x) + seg0dir.x * (seg0start.y - seg1start.y)) / (-seg1dir.x * seg0dir.y + seg0dir.x * seg1dir.y);
        double t = ( seg1dir.x * (seg0start.y - seg1start.y) - seg1dir.y * (seg0start.x - seg1start.x)) / (-seg1dir.x * seg0dir.y + seg0dir.x * seg1dir.y);
        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected, return the point of intersection
            return new Vec2(seg0start.x + (t * seg0dir.x), seg0start.y + (t * seg0dir.y));
        }
        return null;
    }
    public static boolean intersectCircleSegment(Vec2 circleCenter, double circleRadius, Vec2 segStart, Vec2 segDir) {
        double dst = distancePointToSegment(circleCenter, segStart, segDir);
        return (dst < circleRadius);
    }
    public static boolean intersectCircleCapsule(Vec2 circleCenter, double circleRadius, Vec2 capSegStart, Vec2 capSegDir, double capRadius) {
        double dst = distancePointToSegment(circleCenter, capSegStart, capSegDir);
        return (dst < circleRadius + capRadius);
    }

    // Distances (returns null if no intersection, vec2 with intersection otherwise)...
    public static double distancePointToLine(Vec2 point, Vec2 linePoint, Vec2 lineDirUnit) {
        Vec2 v = Vec2.subtract(point, linePoint);
        Vec2 u = Vec2.subtract(v, Vec2.multiply(lineDirUnit, Vec2.dot(v, lineDirUnit)));
        return u.length();
    }
    public static double distancePointToSegment(Vec2 point, Vec2 segStart, Vec2 segDir) {
        Vec2 v = Vec2.subtract(point, segStart);
        double segDirLen = segDir.length();
        Vec2 segDirUnit = Vec2.divide(segDir, segDirLen);
        Vec2 closestPt = Vec2.add(segStart, Vec2.multiply(segDirUnit, Util.clamp(Vec2.dot(v, segDirUnit), 0, segDirLen)));
        return Vec2.distance(point, closestPt);
    }
    public static double distancePointToPlane(Vec2 point, Vec2 linePoint, Vec2 lineNormalUnit) {
        Vec2 v = Vec2.subtract(point, linePoint);
        return Vec2.dot(v, lineNormalUnit);
    }

    // Graphics utils
    public static boolean isDefaultContext() {
        return GL.getCapabilities().OpenGL32;
    }
    public static void toBuffer(Vec2 v, FloatBuffer buffer) {
        buffer.put((float)v.x).put((float)v.y);
        buffer.flip();
    }
    public static void toBuffer(Vec3 v, FloatBuffer buffer) {
        buffer.put((float)v.x).put((float)v.y).put((float)v.z);
        buffer.flip();
    }
    public static void toBuffer(Vec4 v, FloatBuffer buffer) {
        buffer.put((float)v.x).put((float)v.y).put((float)v.z).put((float)v.w);
        buffer.flip();
    }
    public static void toBuffer(Mat3x3 m, FloatBuffer buffer) {
        buffer.put((float)m.m[0][0]).put((float)m.m[1][0]).put((float)m.m[2][0]);
        buffer.put((float)m.m[0][1]).put((float)m.m[1][1]).put((float)m.m[2][1]);
        buffer.put((float)m.m[0][2]).put((float)m.m[1][2]).put((float)m.m[2][2]);
        buffer.flip();
    }
    public static void toBuffer(Mat4x4 m, FloatBuffer buffer) {
        buffer.put((float)m.m[0][0]).put((float)m.m[1][0]).put((float)m.m[2][0]).put((float)m.m[3][0]);
        buffer.put((float)m.m[0][1]).put((float)m.m[1][1]).put((float)m.m[2][1]).put((float)m.m[3][1]);
        buffer.put((float)m.m[0][2]).put((float)m.m[1][2]).put((float)m.m[2][2]).put((float)m.m[3][2]);
        buffer.put((float)m.m[0][3]).put((float)m.m[1][3]).put((float)m.m[2][3]).put((float)m.m[3][3]);
        buffer.flip();
    }
}