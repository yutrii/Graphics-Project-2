in vec3 N;
in vec4 v;
in vec2 texture;

uniform sampler2D colour;

void main(void){
   vec3 normal, lightDir;

   //These all have RGBA components.
   vec4 diffuse, globalAmbient,ambient;
   float NdotL;

   globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
   ambient = gl_LightSource[0].ambient * gl_FrontMaterial.ambient;

   normal = normalize(N);
   
   //Direction from fragment to the light
   lightDir = normalize(vec3(gl_LightSource[0].position - v));
	NdotL = max(dot(n, lightDir), 0.0);
   diffuse = NdotL * gl_LightSource[0].diffuse * gl_FrontMaterial.diffuse;

  
  	vec4 specular = vec4(0.0, 0.0, 0.0, 1);
   gl_FragColor = (gl_FrontMaterial.emission + globalAmbient + ambient + diffuse) + specular;
   

}