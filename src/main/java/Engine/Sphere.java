package Engine;

import org.joml.*;
//import org.lwjgl.assimp.AIFace;
//import org.lwjgl.assimp.AIMesh;
//import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.*;

import java.lang.Math;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Sphere extends Circle{
    Float radiusZ;

    List<Integer> index;
    int ibo;

    List<Vector3f> normal;
    int nbo;
    AIScene scene;

    public static int globalInt = 0;

//    public static int globalInt = 0;

    public Sphere(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, List<Float> centerPoint, Float radiusX, Float radiusY, Float radiusZ, String filename) {
        super(shaderModuleDataList, vertices, color, centerPoint, radiusX, radiusY);
        this.radiusZ = radiusZ;
//        createBox();
//        scene = Assimp.aiImportFile("D:\\KULIAH\\semester 4\\grafkom\\GrafkomB-master specular\\GrafkomB-master\\src\\main\\resources\\models\\Charizard - Full.fbx", Assimp.aiProcess_Triangulate | Assimp.aiProcess_FlipUVs);
//        loadFbxFiles();
//        loadObjModel("/models/home1.obj");
        loadObjModel("/models/"+filename);
        System.out.println(filename);
//        loadObjModel("/models/pagar.obj");
        setIbo();
        setupVAOVBO();
        System.out.println(globalInt);
        globalInt++;
    }

    public void setIbo(){
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.listoInt(index), GL_STATIC_DRAW);
    }

    @Override
    public void draw(Camera camera, Projection projection,int select, Vector3f tmp){
        if(select == 1){
            drawSetup(camera, projection);
        } else if (select == 2) {
            drawSetupLamp(camera,projection, tmp);
        } else if (select == 3) {
            drawSetup3(camera,projection);
        }
        // Bind IBO dan draw
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ibo);
        glDrawElements(GL_TRIANGLES, index.size(), GL_UNSIGNED_INT, 0);
        // kalo mau lingkaran tanpa fill ganti ke GL_LINES
    }

    public void createBox(){
        vertices.clear();
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();
        //Titik 1 kiri atas belakang
        temp.x = centerPoint.get(0) - radiusX / 2;
        temp.y = centerPoint.get(1) + radiusY / 2;
        temp.z = centerPoint.get(2) - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 2 kiri bawah belakang
        temp.x = centerPoint.get(0) - radiusX / 2;
        temp.y = centerPoint.get(1) - radiusY / 2;
        temp.z = centerPoint.get(2) - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 3 kanan bawah belakang
        temp.x = centerPoint.get(0) + radiusX / 2;
        temp.y = centerPoint.get(1) - radiusY / 2;
        temp.z = centerPoint.get(2) - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 4 kanan atas belakang
        temp.x = centerPoint.get(0) + radiusX / 2;
        temp.y = centerPoint.get(1) + radiusY / 2;
        temp.z = centerPoint.get(2) - radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 5 kiri atas depan
        temp.x = centerPoint.get(0) - radiusX / 2;
        temp.y = centerPoint.get(1) + radiusY / 2;
        temp.z = centerPoint.get(2) + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 6 kiri bawah depan
        temp.x = centerPoint.get(0) - radiusX / 2;
        temp.y = centerPoint.get(1) - radiusY / 2;
        temp.z = centerPoint.get(2) + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 7 kanan bawah depan
        temp.x = centerPoint.get(0) + radiusX / 2;
        temp.y = centerPoint.get(1) - radiusY / 2;
        temp.z = centerPoint.get(2) + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 8 kanan atas depan
        temp.x = centerPoint.get(0) + radiusX / 2;
        temp.y = centerPoint.get(1) + radiusY / 2;
        temp.z = centerPoint.get(2) + radiusZ / 2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //kotak belakang
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(2));

        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(0));
        //kotak depan
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(4));
        //kotak samping kiri
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(4));

        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(4));
        //kotak samping kanan
        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(2));

        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(7));
        //kotak bawah
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(1));
        //kotak atas
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(7));

        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(3));

        normal = new ArrayList<>(Arrays.asList(
                //belakang
                new Vector3f(0.0f,0.0f,-1.0f),
                new Vector3f(0.0f,0.0f,-1.0f),
                new Vector3f(0.0f,0.0f,-1.0f),
                new Vector3f(0.0f,0.0f,-1.0f),
                new Vector3f(0.0f,0.0f,-1.0f),
                new Vector3f(0.0f,0.0f,-1.0f),
                //depan
                new Vector3f(0.0f,0.0f,1.0f),
                new Vector3f(0.0f,0.0f,1.0f),
                new Vector3f(0.0f,0.0f,1.0f),
                new Vector3f(0.0f,0.0f,1.0f),
                new Vector3f(0.0f,0.0f,1.0f),
                new Vector3f(0.0f,0.0f,1.0f),
                //kiri
                new Vector3f(-1.0f,0.0f,0.0f),
                new Vector3f(-1.0f,0.0f,0.0f),
                new Vector3f(-1.0f,0.0f,0.0f),
                new Vector3f(-1.0f,0.0f,0.0f),
                new Vector3f(-1.0f,0.0f,0.0f),
                new Vector3f(-1.0f,0.0f,0.0f),
                //kanan
                new Vector3f(1.0f,0.0f,0.0f),
                new Vector3f(1.0f,0.0f,0.0f),
                new Vector3f(1.0f,0.0f,0.0f),
                new Vector3f(1.0f,0.0f,0.0f),
                new Vector3f(1.0f,0.0f,0.0f),
                new Vector3f(1.0f,0.0f,0.0f),
                //bawah
                new Vector3f(0.0f,-1.0f,0.0f),
                new Vector3f(0.0f,-1.0f,0.0f),
                new Vector3f(0.0f,-1.0f,0.0f),
                new Vector3f(0.0f,-1.0f,0.0f),
                new Vector3f(0.0f,-1.0f,0.0f),
                new Vector3f(0.0f,-1.0f,0.0f),
                //atas
                new Vector3f(0.0f,1.0f,0.0f),
                new Vector3f(0.0f,1.0f,0.0f),
                new Vector3f(0.0f,1.0f,0.0f),
                new Vector3f(0.0f,1.0f,0.0f),
                new Vector3f(0.0f,1.0f,0.0f),
                new Vector3f(0.0f,1.0f,0.0f)
        ));
    }
    public void createSphere(){
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();
        int stackCount = 18, sectorCount = 36;
        float x,y,z,xy,nx,ny,nz;
        float sectorStep = (float)(2* Math.PI )/ sectorCount; //sector count
        float stackStep = (float)Math.PI / stackCount; // stack count
        float sectorAngle, stackAngle;

        //titik persegi
        for(int i=0;i<=stackCount;i++){
            stackAngle = (float)Math.PI/2 - i * stackStep;
            xy = (float) (0.5f * Math.cos(stackAngle));
            z = (float) (0.5f * Math.sin(stackAngle));
            for(int j=0;j<sectorCount;j++){
                sectorAngle = j * sectorStep;
                x = (float) (xy * Math.cos(sectorAngle));
                y = (float) (xy * Math.sin(sectorAngle));
                temp.add(new Vector3f(x,y,z));
            }
        }
        vertices = temp;

        int k1, k2;
        ArrayList<Integer> temp_indices = new ArrayList<>();
        for(int i=0;i<stackCount;i++){
            k1 = i * (sectorCount + 1);
            k2 = k1 + sectorCount + 1;

            for(int j=0;j<sectorCount;++j, ++k1, ++k2){
                if(i != 0){
                    temp_indices.add(k1);
                    temp_indices.add(k2);
                    temp_indices.add(k1+1);
                }
                if(i!=(18-1)){
                    temp_indices.add(k1+1);
                    temp_indices.add(k2);
                    temp_indices.add(k2+1);
                }
            }
        }

        this.index = temp_indices;
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,
                ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,
                Utils.listoInt(index), GL_STATIC_DRAW);

    }
    public void setupVAOVBO(){
        super.setupVAOVBO();

        //nbo
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
                GL_STATIC_DRAW);
//        uniformsMap.createUniform("lightColor");
//        uniformsMap.createUniform("lightPos");
    }
    public void drawSetupLamp(Camera camera, Projection projection, Vector3f lampPosition){
        super.drawSetup(camera,projection);

        // Bind NBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);

//        uniformsMap.setUniform("lightColor",
//                new Vector3f(1.0f,1.0f,0.0f));
//        uniformsMap.setUniform("lightPos",
//                new Vector3f(1.0f,30.0f,4.0f));
//        uniformsMap.setUniform("viewPos",camera.getPosition());

        // directional light
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.5f, -1.0f, -0.3f));  // Adjust the direction of the light
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.3f,0.3f,0.3f));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(.8f,.8f,0.8f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(.2f,.2f,.2f));

        Vector3f[] _pointLightPositions = {
                new Vector3f(-1.922E-2f, -2.091E-3f, -5.112E-3f),
                new Vector3f(1.358E-3f, -1.256E-3f, -1.991E-2f),
                new Vector3f(-1.999E-2f,  6.631E-4f,  1.776E-4f),
                new Vector3f(-1.883E-2f,  6.631E-4f,  6.708E-3f),
                new Vector3f(-1.391E-3f, -6.631E-4f,  1.994E-2f),
                new Vector3f(9.353E-3f, -5.235E-4f, -1.767E-2f),
                new Vector3f(-1.115E-2f, -4.022E-3f,  1.611E-2f),
                new Vector3f(-1.892E-2f, -2.229E-3f,  6.078E-3f),
                new Vector3f(5.556E-4f,  1.395E-3f, -1.994E-2f)
        };
//        Vector3f[] _pointLightPositions = {
//                new Vector3f(4.961E+0f , 3.988E+0f ,-5.000E+0f),
//                new Vector3f(5.104E+0f,  3.304E+0f,  8.138E+0f),   // Increased spread by changing Z-coordinate
//                new Vector3f(2.861E+0f,  2.559E+0f,  8.047E+0f),  // Increased spread by changing Z-coordinate
//                new Vector3f(-5.531E+0f,  3.613E+0f,  6.062E+0f)    // Increased spread by changing Z-coordinate
//        };

        for(int i = 0; i < _pointLightPositions.length; i++){
            uniformsMap.setUniform("pointLight["+ i +"].position",_pointLightPositions[i]);
            uniformsMap.setUniform("pointLight[" + i + "].ambient", new Vector3f(0.3f, 0.3f, 0.3f));
            uniformsMap.setUniform("pointLight[" + i + "].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            uniformsMap.setUniform("pointLight[" + i + "].specular", new Vector3f(0.2f, 0.2f, 0.2f));
            uniformsMap.setUniform("pointLight[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLight[" + i + "].linear", 0.045f);      // Increase linear attenuation
            uniformsMap.setUniform("pointLight[" + i + "].quadratic", 0.0075f);

        }

        //spotlight
//        uniformsMap.setUniform("spotLight.position",camera.getPosition());
//        uniformsMap.setUniform("spotLight.direction",camera.getDirection());
//        uniformsMap.setUniform("spotLight.ambient",new Vector3f(0.3f,0.3f,.3f));
//        uniformsMap.setUniform("spotLight.diffuse",new Vector3f(.8f,.8f,.8f));
//        uniformsMap.setUniform("spotLight.specular",new Vector3f(.2f,.2f,.2f));
//        uniformsMap.setUniform("spotLight.constant",1.0f);
//        uniformsMap.setUniform("spotLight.linear",0.09f);
//        uniformsMap.setUniform("spotLight.quadratic",0.032f);
//        uniformsMap.setUniform("spotLight.cutOff",(float)Math.cos(Math.toRadians(12.5f)));
//        uniformsMap.setUniform("spotLight.outerCutOff",(float)Math.cos(Math.toRadians(12.5f)));

//        uniformsMap.setUniform("viewPos",camera.getPosition());
        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3,
                GL_FLOAT,
                false,
                0, 0);

    }
    public void drawSetup(Camera camera, Projection projection){
        super.drawSetup(camera,projection);

        // Bind NBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);

//        uniformsMap.setUniform("lightColor",
//                new Vector3f(1.0f,1.0f,0.0f));
//        uniformsMap.setUniform("lightPos",
//                new Vector3f(1.0f,30.0f,4.0f));
//        uniformsMap.setUniform("viewPos",camera.getPosition());

        // directional light
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.2f,-1.0f,-0.3f));
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.05f,0.05f,0.05f));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.4f,0.4f,0.4f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(0.5f,0.5f,0.5f));

        Vector3f[] _pointLightPositions = {
                new Vector3f(0.7f,0.2f,2.0f),
                new Vector3f(2.3f,-3.3f,-4.0f),
                new Vector3f(-4.0f,2.0f,-12.0f),
                new Vector3f(0.0f,0.0f,-3.0f)
        };
        for (int i=0;i<_pointLightPositions.length;i++){
            uniformsMap.setUniform("pointLights["+i+"].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLights["+ i +"].ambient", new Vector3f(0.05f,0.05f,0.05f));
            uniformsMap.setUniform("pointLights["+ i +"].diffuse", new Vector3f(0.8f,0.8f,0.8f));
            uniformsMap.setUniform("pointLights["+ i +"].specular", new Vector3f(1.0f,1.0f,1.0f));
            uniformsMap.setUniform("pointLights["+ i +"].constant",1.0f );
            uniformsMap.setUniform("pointLights["+ i +"].linear", 0.09f);
            uniformsMap.setUniform("pointLights["+ i +"].quadratic", 0.032f);
        }
        // spotlight
        uniformsMap.setUniform("spotLight.position", camera.getPosition());
        uniformsMap.setUniform("spotLight.direction", camera.getDirection());
        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f,0.0f,0.0f));
        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(1.0f,1.0f,1.0f));
        uniformsMap.setUniform("spotLight.specular", new Vector3f(1.0f,1.0f,1.0f));
        uniformsMap.setUniform("spotLight.constant", 1.0f);
        uniformsMap.setUniform("spotLight.linear", 0.09f);
        uniformsMap.setUniform("spotLight.quadratic", 0.032f);
        uniformsMap.setUniform("spotLight.cutOff", (float)Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("spotLight.outerCutOff", (float)Math.cos(Math.toRadians(12.5f)));

        uniformsMap.setUniform("viewPos",camera.getPosition());

    }
    public void drawSetup3(Camera camera, Projection projection){
        super.drawSetup(camera,projection);

        // Bind NBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);

//        uniformsMap.setUniform("lightColor",
//                new Vector3f(1.0f,1.0f,0.0f));
//        uniformsMap.setUniform("lightPos",
//                new Vector3f(1.0f,30.0f,4.0f));
//        uniformsMap.setUniform("viewPos",camera.getPosition());

        // directional light
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.2f,-1.0f,-0.3f));
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.05f,0.05f,0.05f));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.4f,0.4f,0.4f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(0.5f,0.5f,0.5f));

        Vector3f[] _pointLightPositions = {
                new Vector3f(4.970E+0f,4.089E+0f,-5.114E+0f),
                new Vector3f(-1.966E-2f,-3.610E-3f,-5.840E-4f),
                new Vector3f(-1.478E-2f,-1.320E-2f,-2.687E-3f),
                new Vector3f(-1.481E-3f,7.654E-3f,-1.842E-2f),
                new Vector3f(1.387E-4f,-9.421E-4f,1.998E-2f),
                new Vector3f(-1.934E+1f, 9.402E-1f,  1.057E+1f),
                new Vector3f( 6.863E-3f, -6.313E-3f , 1.769E-2f),
                new Vector3f( -2.335E+0f,  1.432E+0f,  7.041E+0f),
//                new Vector3f(-1.922E-2f, -2.091E-3f, -5.112E-3f),
//                new Vector3f(1.358E-3f, -1.256E-3f, -1.991E-2f),
//                new Vector3f(-1.999E-2f,  6.631E-4f,  1.776E-4f),
//                new Vector3f(-1.883E-2f,  6.631E-4f,  6.708E-3f),
//                new Vector3f(-1.391E-3f, -6.631E-4f,  1.994E-2f),
//                new Vector3f(9.353E-3f, -5.235E-4f, -1.767E-2f),
//                new Vector3f(-1.115E-2f, -4.022E-3f,  1.611E-2f),
//                new Vector3f(-1.892E-2f, -2.229E-3f,  6.078E-3f),
//                new Vector3f(5.556E-4f,  1.395E-3f, -1.994E-2f)
        };
//        Vector3f[] _pointLightPositions2 = {
//                new Vector3f(-4.938E-2f,  7.736E-3f,  1.382E-3f),
//                new Vector3f(-1.325E-2f,  3.052E-3f,  4.812E-2f),
//                new Vector3f( 6.788E-3f, -1.328E-2f, -4.772E-2f),
//                new Vector3f(-5.474E+0f,  3.545E+0f,  6.035E+0f),
//                new Vector3f(2.710E+0f,  2.633E+0f,  8.010E+0f),
//                new Vector3f( 1.586E+0f, 4.434E+0f, -1.992E+0f),
//                new Vector3f(-1.934E+1f, 9.402E-1f,  1.057E+1f),
//                new Vector3f( 6.863E-3f, -6.313E-3f , 1.769E-2f),
//                new Vector3f( -2.335E+0f,  1.432E+0f,  7.041E+0f),
////                new Vector3f(-1.922E-2f, -2.091E-3f, -5.112E-3f),
////                new Vector3f(1.358E-3f, -1.256E-3f, -1.991E-2f),
////                new Vector3f(-1.999E-2f,  6.631E-4f,  1.776E-4f),
////                new Vector3f(-1.883E-2f,  6.631E-4f,  6.708E-3f),
////                new Vector3f(-1.391E-3f, -6.631E-4f,  1.994E-2f),
////                new Vector3f(9.353E-3f, -5.235E-4f, -1.767E-2f),
////                new Vector3f(-1.115E-2f, -4.022E-3f,  1.611E-2f),
////                new Vector3f(-1.892E-2f, -2.229E-3f,  6.078E-3f),
////                new Vector3f(5.556E-4f,  1.395E-3f, -1.994E-2f)
//        };
        for (int i=0;i<_pointLightPositions.length;i++){
            uniformsMap.setUniform("pointLight["+i+"].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLight["+ i +"].ambient", new Vector3f(0.06f,0.06f,0.06f));
            uniformsMap.setUniform("pointLight["+ i +"].diffuse", new Vector3f(0.8f,0.8f,0.8f));
            uniformsMap.setUniform("pointLight["+ i +"].specular", new Vector3f(0f,0f,0f));
            uniformsMap.setUniform("pointLight["+ i +"].constant",1.0f );
            uniformsMap.setUniform("pointLight["+ i +"].linear", 0.09f);
            uniformsMap.setUniform("pointLight["+ i +"].quadratic", 0.032f);
        }
        // spotlight
//

    }

    public void loadFbxFiles(){
        index = new ArrayList<>();
        normal = new ArrayList<>();
        int numMeshes = scene.mNumMeshes();
        for (int x = 0; x <numMeshes; x++) { //kalo ada beberapa model
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(x));

            // vertices
            AIVector3D.Buffer verticesBuffer = mesh.mVertices();
            int numVertices = mesh.mNumVertices();


            for (int i = 0; i < numVertices; i++) {
                AIVector3D vertex = verticesBuffer.get(i);
                Vector3f verticesVec = new Vector3f(vertex.x(), vertex.y(), vertex.z());
                vertices.add(verticesVec);
            }

            //  normal
            AIVector3D.Buffer normalsBuffer = mesh.mNormals();
            int numNormals = mesh.mNumVertices();

            for (int i = 0; i < numNormals; i++) {
                AIVector3D vertex = normalsBuffer.get(i);
                Vector3f verticesVec = new Vector3f(vertex.x(), vertex.y(), vertex.z());
                normal.add(verticesVec);
            }

            //indices
            AIFace.Buffer facesBuffer = mesh.mFaces();
            int numFaces = mesh.mNumFaces();

            for (int i = 0; i < numFaces; i++) {
                AIFace face = facesBuffer.get(i);
                int numIndices = face.mNumIndices();
                for (int j = 0; j < numIndices; j++) {
                    int index = face.mIndices().get(j);
                    this.index.add(index);
                }
                System.out.println();
            }
        }

    }

    public void loadObjModel(String fileName){
        List<String> lines = Utils.readAllLines(fileName);

//        List<Vector3f> vertices = new ArrayList<>();
//        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();
        vertices.clear();
        normal = new ArrayList<>();
        index = new ArrayList<>();
        for (String line: lines){
            String[] tokens = line.split("\\s+");
            switch (tokens[0]){
                case "v":
                    // vertices
                    Vector3f verticesVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(verticesVec);
                    break;
                case "vt":
                    Vector2f textureVec = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(textureVec);
                    break;
                case "vn":
                    Vector3f normalsVec = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normal.add(normalsVec);
                    break;
                case "f":
                    processFace(tokens[1],  faces);
                    processFace(tokens[2],  faces);
                    processFace(tokens[3],  faces);
                    break;
                default:
                    break;
            }
//            System.out.println(vertices);
//            System.out.println(normals);
//            System.out.println(textures);
//            System.out.println(faces);
        }
        List<Integer> indices = new ArrayList<>();
        float[] verticesArr = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f pos : vertices){
            verticesArr[i *3] = pos.x;
            verticesArr[i *3 + 1] = pos.y;
            verticesArr[i *3 + 2] = pos.z;
            i++;
        }
        float[] texCoordArr = new float[vertices.size() * 2];
        float[] normalArr = new float[vertices.size() * 3];

        for (Vector3i face: faces){
            processVertex(face.x, face.y,face.z, textures, normal, indices,texCoordArr, normalArr);
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        this.vertices = Utils.floatToList(verticesArr);
        this.normal = Utils.floatToList(normalArr);
        this.index = Utils.intToList(indicesArr);
    }

    private static void processVertex(int pos,int texCoord, int normal, List<Vector2f> texCoordList, List<Vector3f> normalList,
                                      List<Integer> indicesList, float[] texCoordArr, float[] normalArr){
        indicesList.add(pos);
        if (texCoord >= 0){
            Vector2f texCoordVec = texCoordList.get(texCoord);
            texCoordArr[pos * 2]= texCoordVec.x;
            texCoordArr[pos * 2 + 1]= texCoordVec.y;

        }

        if (normal >= 0){
            Vector3f normalVec = normalList.get(normal);
            normalArr[pos * 3] = normalVec.x;
            normalArr[pos * 3 + 1] = normalVec.y;
            normalArr[pos * 3 + 2] = normalVec.z;
        }
    }

    public void moveBackward(Float amount) {
        Matrix4f translationMatrix = new Matrix4f().translate(-dir.x * amount, -dir.y * amount, -dir.z * amount);
        model = model.mul(translationMatrix);
    }
    public void moveForward(Float amount) {
        Matrix4f translationMatrix = new Matrix4f().translate(dir.x * amount, dir.y * amount, dir.z * amount);
        model = model.mul(translationMatrix);
    }

    private static void processFace(String token, List<Vector3i> faces){
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int pos = -1, coords= - 1, normal = -1;
        pos = Integer.parseInt(lineToken[0])-1;
        if (length > 1){
            String textCoord = lineToken[1];
            coords = textCoord.length() > 0 ? Integer.parseInt(textCoord)- 1: -1;
            if (length > 2){
                normal = Integer.parseInt(lineToken[2])-1;
            }
        }
        Vector3i facesVec = new Vector3i(pos, coords, normal);
        faces.add(facesVec);
    }
//    public void draw(){
//        drawSetup();
//        //Bind IBO & draw
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glDrawElements(GL_TRIANGLES,
//                index.size(),
//                GL_UNSIGNED_INT, 0);
//    }


}