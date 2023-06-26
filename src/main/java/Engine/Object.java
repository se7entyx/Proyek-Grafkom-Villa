package Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Object extends ShaderProgram{
    List<Vector3f> vertices;
    int vao;
    int vbo;
    Vector4f color;
    UniformsMap uniformsMap;

    List<Vector3f> verticesColor;
    int vboColor;

    Matrix4f model;

    List<Object> childObject;

    public List<Object> getChildObject() {
        return childObject;
    }

    public void setChildObject(List<Object> childObject) {
        this.childObject = childObject;
    }

    Vector3f dir = new Vector3f(-1,0,0);

    public Vector3f getDir() {
        return dir;
    }

    public void setDir(Vector3f dir) {
        this.dir = dir;
    }

    public Vector3f updateCenterPoint(){
        Vector3f centerTemp = new Vector3f();
        model.transformPosition(0.0f,0.0f,0.0f,centerTemp);
        return centerTemp;
    }
    public Object(List<ShaderModuleData> shaderModuleDataList,
                  List<Vector3f> vertices,
                  Vector4f color) {
        super(shaderModuleDataList);
        this.vertices = vertices;
//        setupVAOVBO();
        this.color = color;
        uniformsMap = new UniformsMap(getProgramId());
//        uniformsMap.createUniform(
//                "uni_color");
//        uniformsMap.createUniform(
//                "model");
//        uniformsMap.createUniform(
//                "view");
//        uniformsMap.createUniform(
//                "projection");
        model = new Matrix4f();
        childObject = new ArrayList<>();
    }
    public Object(List<ShaderModuleData> shaderModuleDataList,
                  List<Vector3f> vertices,
                  List<Vector3f> verticesColor) {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.verticesColor = verticesColor;
        setupVAOVBOWithVerticesColor();
    }
    public void setupVAOVBO(){
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        //mengirim vertices ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);
    }
    public void setupVAOVBOWithVerticesColor(){
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        //mengirim vertices ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);
        //set vboColor
        vboColor = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        //mengirim vbocolor ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(verticesColor),
                GL_STATIC_DRAW);
    }
    public void drawSetup(Camera camera, Projection projection){
        bind();
        uniformsMap.setUniform(
                "uni_color", color);
        uniformsMap.setUniform(
                "model", model);
        uniformsMap.setUniform(
                "view", camera.getViewMatrix());
        uniformsMap.setUniform(
                "projection", projection.getProjMatrix());
        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0,
                3, GL_FLOAT,
                false,
                0, 0);
    }
    public void drawSetupWithVerticesColor(){
        bind();
        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0,
                3, GL_FLOAT,
                false,
                0, 0);
        // Bind VBOColor
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);
    }
    public void draw(Camera camera,Projection projection, int select, Vector3f tmp){
        drawSetup(camera,projection);
        // Draw the vertices
        glLineWidth(10);
        glPointSize(10);
        //GL_TRIANGLES
        //GL_LINE_LOOP
        //GL_LINE_STRIP
        //GL_LINES
        //GL_POINTS
        //GL_TRIANGLE_FAN
        glDrawArrays(GL_TRIANGLES, 0,
                vertices.size());
        for(Object child:childObject){
            child.draw(camera,projection,select,tmp);
        }
    }

    public void drawWithVerticesColor(){
        drawSetupWithVerticesColor();
        // Draw the vertices
        glLineWidth(10);
        glPointSize(10);
        //GL_TRIANGLES
        //GL_LINE_LOOP
        //GL_LINE_STRIP
        //GL_LINES
        //GL_POINTS
        //GL_TRIANGLE_FAN
        glDrawArrays(GL_TRIANGLES, 0,
                vertices.size());
    }
    public void moveBackward(Float amount) {
        Matrix4f translationMatrix = new Matrix4f().translate(-dir.x * amount, -dir.y * amount, -dir.z * amount);
        model = model.mul(translationMatrix);
    }
    public void moveForward(Float amount) {
        Matrix4f translationMatrix = new Matrix4f().translate(dir.x * amount, dir.y * amount, dir.z * amount);
        model = model.mul(translationMatrix);
    }

    public Vector3f BoundingRectMin(){
        float min_x = Float.POSITIVE_INFINITY;
        float min_y = Float.POSITIVE_INFINITY;
        float min_z = Float.POSITIVE_INFINITY;

        for (Vector3f object : this.vertices) {
            Vector4f transformed = new Vector4f(object,1f);
            model.transform(transformed);
            min_x = Math.min(min_x, transformed.x);
            min_y = Math.min(min_y, transformed.y);
            min_z = Math.min(min_z, transformed.z);
        }
        return new Vector3f(min_x,min_y,min_z);

    }
    public Vector3f BoundingRectMax(){
        float max_x = Float.NEGATIVE_INFINITY;
        float max_y = Float.NEGATIVE_INFINITY;
        float max_z = Float.NEGATIVE_INFINITY;

        for (Vector3f object : this.vertices) {
            Vector4f transformed = new Vector4f(object,1f);
            model.transform(transformed);
            max_x = Math.max(max_x, transformed.x);
            max_y = Math.max(max_y, transformed.y);
            max_z = Math.max(max_z, transformed.z);
        }
        return new Vector3f(max_x,max_y,max_z);
    }

    public void drawLine(){
//        drawSetup();
        // Draw the vertices
        glLineWidth(1);
        glPointSize(1);
        glDrawArrays(GL_LINE_STRIP, 0,
                vertices.size());
    }
    public void addVertices(Vector3f newVector){
        vertices.add(newVector);
        setupVAOVBO();
    }
    public void translateObject(Float offsetX,Float offsetY,Float offsetZ){
        model = new Matrix4f().translate(offsetX,offsetY,offsetZ).mul(new Matrix4f(model));
        for (Object child:childObject){
            child.translateObject(offsetX,offsetY,offsetZ);
        }
    }
    public void rotateObject(Float degree, Float offsetX,Float offsetY,Float offsetZ){
        model = new Matrix4f().rotate(degree,offsetX,offsetY,offsetZ).mul(new Matrix4f(model));
        for (Object child:childObject){
            child.rotateObject(degree,offsetX,offsetY,offsetZ);
        }
    }
    public void setRotate(){
        model = new Matrix4f().rotate((float) Math.toRadians(1f),0f,(float) Math.toRadians(-90f),0f).mul(model);
    }
    public void scaleObject(Float x,Float y,Float z){
        model = new Matrix4f().scale(x,y,z).mul(new Matrix4f(model));
        for (Object child:childObject){
            child.scaleObject(x,y,z);
        }
    }
}