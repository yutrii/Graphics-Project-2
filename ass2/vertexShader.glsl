out vec3 N; //these are outputs not inputs.
out vec4 v;

void main(void){
    v = gl_ModelViewMatrix * gl_Vertex;
    N = vec3(normalize(gl_NormalMatrix * gl_Normal));

    //output needs to be in CVV coordinates - 
    //ie multiplied by projection matrix
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}