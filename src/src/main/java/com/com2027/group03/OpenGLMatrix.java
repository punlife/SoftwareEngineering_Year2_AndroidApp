package com.com2027.group03;

import android.opengl.Matrix;

/**
 * Created by Matus on 21-Feb-17.
 */
public class OpenGLMatrix {
    // Copied from: https://github.com/matusnovak/fineframework/blob/master/include/ffw/math/mat4.h
    public float[] ptr = new float[16];

    public OpenGLMatrix(){
        setIdentity(1.0f);
    }

    public OpenGLMatrix(float f){
        setIdentity(f);
    }

    public void setIdentity(float f){
        ptr[0] = f;     ptr[4] = 0.0f;  ptr[8] = 0.0f;  ptr[12] = 0.0f;
        ptr[1] = 0.0f;  ptr[5] = f;     ptr[9] = 0.0f;  ptr[13] = 0.0f;
        ptr[2] = 0.0f;  ptr[6] = 0.0f;  ptr[10] = f;    ptr[14] = 0.0f;
        ptr[3] = 0.0f;  ptr[7] = 0.0f;  ptr[11] = 0.0f; ptr[15] = f;
    }

    public void transpose() {
        float m04 = ptr[4];
        float m08 = ptr[8];
        float m09 = ptr[9];
        float m12 = ptr[12];
        float m13 = ptr[13];
        float m14 = ptr[14];
        ptr[4] = ptr[1];
        ptr[8] = ptr[2];
        ptr[9] = ptr[6];
        ptr[12] = ptr[3];
        ptr[13] = ptr[7];
        ptr[14] = ptr[11];
        ptr[1] = m04;
        ptr[2] = m08;
        ptr[6] = m09;
        ptr[3] = m12;
        ptr[7] = m13;
        ptr[11] = m14;
    }

    public void inverse(){
        float[] inv = new float[16];
        inv[0] = ptr[5] * ptr[10] * ptr[15] - ptr[5] * ptr[11] * ptr[14] - ptr[9] * ptr[6] * ptr[15] + ptr[9] * ptr[7] * ptr[14] + ptr[13] * ptr[6] * ptr[11] - ptr[13] * ptr[7] * ptr[10];
        inv[4] = -ptr[4] * ptr[10] * ptr[15] + ptr[4] * ptr[11] * ptr[14] + ptr[8] * ptr[6] * ptr[15] - ptr[8] * ptr[7] * ptr[14] - ptr[12] * ptr[6] * ptr[11] + ptr[12] * ptr[7] * ptr[10];
        inv[8] = ptr[4] * ptr[9] * ptr[15] - ptr[4] * ptr[11] * ptr[13] - ptr[8] * ptr[5] * ptr[15] + ptr[8] * ptr[7] * ptr[13] + ptr[12] * ptr[5] * ptr[11] - ptr[12] * ptr[7] * ptr[9];
        inv[12] = -ptr[4] * ptr[9] * ptr[14] + ptr[4] * ptr[10] * ptr[13] + ptr[8] * ptr[5] * ptr[14] - ptr[8] * ptr[6] * ptr[13] - ptr[12] * ptr[5] * ptr[10] + ptr[12] * ptr[6] * ptr[9];
        inv[1] = -ptr[1] * ptr[10] * ptr[15] + ptr[1] * ptr[11] * ptr[14] + ptr[9] * ptr[2] * ptr[15] - ptr[9] * ptr[3] * ptr[14] - ptr[13] * ptr[2] * ptr[11] + ptr[13] * ptr[3] * ptr[10];
        inv[5] = ptr[0] * ptr[10] * ptr[15] - ptr[0] * ptr[11] * ptr[14] - ptr[8] * ptr[2] * ptr[15] + ptr[8] * ptr[3] * ptr[14] + ptr[12] * ptr[2] * ptr[11] - ptr[12] * ptr[3] * ptr[10];
        inv[9] = -ptr[0] * ptr[9] * ptr[15] + ptr[0] * ptr[11] * ptr[13] + ptr[8] * ptr[1] * ptr[15] - ptr[8] * ptr[3] * ptr[13] - ptr[12] * ptr[1] * ptr[11] + ptr[12] * ptr[3] * ptr[9];
        inv[13] = ptr[0] * ptr[9] * ptr[14] - ptr[0] * ptr[10] * ptr[13] - ptr[8] * ptr[1] * ptr[14] + ptr[8] * ptr[2] * ptr[13] + ptr[12] * ptr[1] * ptr[10] - ptr[12] * ptr[2] * ptr[9];
        inv[2] = ptr[1] * ptr[6] * ptr[15] - ptr[1] * ptr[7] * ptr[14] - ptr[5] * ptr[2] * ptr[15] + ptr[5] * ptr[3] * ptr[14] + ptr[13] * ptr[2] * ptr[7] - ptr[13] * ptr[3] * ptr[6];
        inv[6] = -ptr[0] * ptr[6] * ptr[15] + ptr[0] * ptr[7] * ptr[14] + ptr[4] * ptr[2] * ptr[15] - ptr[4] * ptr[3] * ptr[14] - ptr[12] * ptr[2] * ptr[7] + ptr[12] * ptr[3] * ptr[6];
        inv[10] = ptr[0] * ptr[5] * ptr[15] - ptr[0] * ptr[7] * ptr[13] - ptr[4] * ptr[1] * ptr[15] + ptr[4] * ptr[3] * ptr[13] + ptr[12] * ptr[1] * ptr[7] - ptr[12] * ptr[3] * ptr[5];
        inv[14] = -ptr[0] * ptr[5] * ptr[14] + ptr[0] * ptr[6] * ptr[13] + ptr[4] * ptr[1] * ptr[14] - ptr[4] * ptr[2] * ptr[13] - ptr[12] * ptr[1] * ptr[6] + ptr[12] * ptr[2] * ptr[5];
        inv[3] = -ptr[1] * ptr[6] * ptr[11] + ptr[1] * ptr[7] * ptr[10] + ptr[5] * ptr[2] * ptr[11] - ptr[5] * ptr[3] * ptr[10] - ptr[9] * ptr[2] * ptr[7] + ptr[9] * ptr[3] * ptr[6];
        inv[7] = ptr[0] * ptr[6] * ptr[11] - ptr[0] * ptr[7] * ptr[10] - ptr[4] * ptr[2] * ptr[11] + ptr[4] * ptr[3] * ptr[10] + ptr[8] * ptr[2] * ptr[7] - ptr[8] * ptr[3] * ptr[6];
        inv[11] = -ptr[0] * ptr[5] * ptr[11] + ptr[0] * ptr[7] * ptr[9] + ptr[4] * ptr[1] * ptr[11] - ptr[4] * ptr[3] * ptr[9] - ptr[8] * ptr[1] * ptr[7] + ptr[8] * ptr[3] * ptr[5];
        inv[15] = ptr[0] * ptr[5] * ptr[10] - ptr[0] * ptr[6] * ptr[9] - ptr[4] * ptr[1] * ptr[10] + ptr[4] * ptr[2] * ptr[9] + ptr[8] * ptr[1] * ptr[6] - ptr[8] * ptr[2] * ptr[5];
        float det = ptr[0] * inv[0] + ptr[1] * inv[4] + ptr[2] * inv[8] + ptr[3] * inv[12];
        det = 1.0f / det;
        for (int i = 0; i < 16; i++)ptr[i] = inv[i] * det;
    }

    // Copied form: https://github.com/matusnovak/fineframework/blob/master/include/ffw/math/mvp.h
    static public OpenGLMatrix makeOrthoMatrix(float left, float right, float bottom, float top, float znear, float zfar){
        OpenGLMatrix mat = new OpenGLMatrix();
        mat.ptr[0] = (2) / (right - left);	        mat.ptr[4] = 0;							    mat.ptr[8] = 0;                      mat.ptr[12] = -(right + left) / (right - left);
        mat.ptr[1] = 0;								mat.ptr[5] = (float)(2) / (top - bottom);	mat.ptr[9] = 0;                      mat.ptr[13] = -(top + bottom) / (top - bottom);
        mat.ptr[2] = 0;								mat.ptr[6] = 0;								mat.ptr[10] = (1) / (zfar - znear);  mat.ptr[14] = -znear / (zfar - znear);
        mat.ptr[3] = 0;								mat.ptr[7] = 0;								mat.ptr[11] = 0;                     mat.ptr[15] = 1;
        return mat;
    }
}
