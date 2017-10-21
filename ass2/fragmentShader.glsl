#version 130
in vec3 N;
in vec4 v;
in vec2 tex;
uniform int light;
uniform sampler2D colour;

void main(void){
   //Ambient light calculations
	vec4 globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
	vec4 ambient = gl_LightSource[0].ambient * gl_FrontMaterial.ambient;
	
	//Diffuse calculations
	vec3 v, normal, lightDir;
	// transform the normal into eye space and normalize
	
	normal = normalize(N);
	
	// Assuming point light, get a vector TO the light
	lightDir = normalize(vec3(gl_LightSource[0].position - v));
	
	float NdotL = max(dot(normal, lightDir), 0.0);
	vec4 diffuse = NdotL * gl_FrontMaterial.diffuse * gl_LightSource[light].diffuse; 
	
	vec4 specular = vec4(0.0,0.0,0.0,1.0);
	vec3 dirToView = normalize(-v);
	vec3 H = normalize(dirToView+lightDir);
	// compute specular term if NdotL is larger than zero
	if (NdotL > 0.0) {
		float NdotHV = max(dot(normal, H),0.0);
		specular = gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(NdotHV,gl_FrontMaterial.shininess);
	} 
   
	//specular = clamp(specular,0,1);
    gl_FragColor = texture2D(colour,tex) * (gl_FrontMaterial.emission + globalAmbient + ambient + diffuse) + specular;

}