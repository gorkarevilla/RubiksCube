/*
 *     This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.gorkarevilla.rubik;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Esta es la capa que interactua con el usuario
 * 
 * @author ramuh
 *
 */
public class VistaCubo extends GLSurfaceView{

	private String _nombreUsuario;

	//El renderizado es el encargado de gestionar el paso de 3D a 2D segun es necesario
	public RenderizadoCubo _renderizado;

	//En x e y se almacenara la posicion en la que se toca la pantalla
	private float _x = 0;
	private float _y = 0;

	//Controla si se encuentra pulsada la pantalla, esto sirve para controlar en que direccion se mueve el dedo
	private boolean _pulsado=false;

	//Controla si hace falta ajustar esa linea
	private boolean _ajustar=false;

	//Almacena el angulo inicial del desplazamiento. Luego se comparara con el angulo de ajuste.
	private float _anguloinicial=0;

	//Proporcion entre el vertical y el horizontal, se usa para poder desplazar mejor en el eje que menos pixeles tiene la pantalla.
	private float _proporcionHoriVert;


	/*
	 * Indica que eje se va a mover siendo:
	 * 
	 * 0: frontal
	 * 1: Izquierda
	 * 2: Superior
	 */
	private int _cara=0;

	//Se almacena la linea en la que se moveria segun las X
	private int _lineaX;

	//Se almacena la linea en la que se moveria segun las Y (tanto para la cara frontal como para izquierda)
	private int _lineaY;

	//Contexto del Activity
	private Activity _context;

	/* Constructor Extendido de GLSurfaceView*/
	public VistaCubo(Context context) {
		super(context);
		_context=(Activity) context;
		_renderizado = new RenderizadoCubo();


		setRenderer(_renderizado);

		this.requestFocus();
		this.setFocusableInTouchMode(true);


	}		

	public void onResume() {
		System.out.println("Reinicia!");

	}


	public void onPause() {

		System.out.println("Menu Pausa");
		//_renderizado._menu._height=this.getHeight();
		//_renderizado._menu._width=this.getWidth();
	}

	/**
	 * Cuando se pulsa un boton esta funcion se encarga de realizar lo necesario
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//System.out.println("Codigo:"+ event.getKeyCode());
		/*
		 * Boton de opciones
		 */
		if(event.getKeyCode()==KeyEvent.KEYCODE_MENU){
			//De momento no tiene opciones
			System.out.println("Opciones!");


			//Debera de abrir el menu de opciones

		}

		/*
		 * Boton Salir
		 */
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			System.out.println("Salir!");
			onPause();

			//Si es true no realizara nada mas, si es false realizara lo que normalmente hace (salir del programa)
			return false;
		}

		/*
		 * Boton de Bloquear Pantalla
		 * Por defecto reinicia el cubo. Esto habria que revisarlo!
		 */
		if(event.getKeyCode()==KeyEvent.KEYCODE_POWER){
			System.out.println("Bloquear!");


		}

		
		//Todos los demas botones realizan su funcion por defecto
		return super.onKeyDown(keyCode, event);

	}
	
	public void crearObjetos(int dimensionCubo, String nombreUsuario) {

		_nombreUsuario = nombreUsuario;
		_renderizado.crearObjetos(dimensionCubo);
		
	}


	/**
	 * Cuando se toque la pantalla esta funcion es la encargada de determinar que hacer
	 */
	public boolean onTouchEvent(final MotionEvent event) {

		_proporcionHoriVert=this.getHeight()/this.getWidth();
		/*
		 * Cuando se toca la pantalla almacenamos la posicion
		 */
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_x = event.getX();
			_y = event.getY();

			//Se tiene que mirar que linea se va a mover segun la posicion del primer toque
			//Tiene que ser la inversa
			_lineaX=_renderizado._uncubo._dimension-1-((int) (_y/ (this.getHeight() / _renderizado._uncubo._dimension)));

			//Se tiene que mirar la linea que se mueve segun el primer toque, esta es absoluta (para la cara derecha habra que restarle la dimension).
			_lineaY=(int) _x/ (this.getWidth()/(_renderizado._uncubo._dimension*2)) ;

		}

		/*
		 *  Cuando se realice un movimiento (se desplaza el dedo) hay que girar el cubo
		 *  
		 */
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			final float xdiferencia = (_x - event.getX());
			final float ydiferencia = (_y - event.getY());

			//RenderizadoCubo.mirarX+=xdiferencia/100;
			//RenderizadoCubo.mirarY+=ydiferencia/200;

			//Si no esta pulsado hay que mirar hacia donde se mueve el dedo (eje X o eje Y)
			if(!_pulsado)
			{
				//Si es mayor la diferencia de x que de y se mueve en el eje de las X
				//System.out.println("COMPARACION!"+Math.abs(xdiferencia)*_proporcionHoriVert+"..."+Math.abs(ydiferencia));
				if((Math.abs(xdiferencia)*_proporcionHoriVert)>Math.abs(ydiferencia))
				{
					_cara=2;
					_pulsado=true;
					_ajustar=true;
				}else
				{

					//if(event.getX()>=this.getWidth()/2)
					if(_lineaY>=_renderizado._uncubo._dimension)
					{
						_cara=0;
					}else
					{
						_cara=1;
					}
					_pulsado=true;
					_ajustar=true;
				}
				//System.out.println("Pulsado1 Cara: "+_cara);

			}




			//synchronized(this)
			queueEvent(new Runnable(){
				public void run() 


				//Creamos un nuevo hilo para no bloquear al principal

				{

					//Calcula la cara y la linea respecto al esquema.
					int linea=0;
					float diferencia=0;
					if(_cara==0){ //Derecha
						//Tiene que ser la inversa
						linea=(_renderizado._uncubo._dimension*2)-1-_lineaY;
						diferencia=ydiferencia;
					}else if(_cara==1){ //Frontal
						linea=_lineaY;
						diferencia=-ydiferencia;
					}else if(_cara==2){ //Superior
						linea=_lineaX;
						diferencia=-xdiferencia;
					}

					//System.out.println("Vista: Cara: "+_cara+" Linea: "+linea);



					if(_ajustar)
					{
						//Se ajusta la linea y se carga el angulo inicial para saber cuantos giros se hacen
						_anguloinicial=_renderizado._uncubo.ajustarReferencias(_cara,linea);
						_ajustar=false;
					}


					//Llamamos a la funcion que realiza giros en OpenGL
					_renderizado._uncubo.girarReferenciasGL(_cara, linea , diferencia);





				}

			});



			_x = event.getX();
			_y = event.getY();
		}

		/*
		 * Cuando se suelta el dedo hay que comprobar si se a girado el cubo o no
		 */
		if (event.getAction() == MotionEvent.ACTION_UP) {
			//Se indica que ya se a levantado el dedo


			//Hacemos una pausa por si el hilo del renderizado no a acabado.
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			_pulsado=false;



			//Se ajustan los valores segun la cara a la que se realize el giro
			int linea=0;
			if(_cara==0){ //Frontal
				//La inversa
				linea=(_renderizado._uncubo._dimension*2)-1-_lineaY;
			}else if(_cara==1){ //Izquierda
				linea=_lineaY;
			}else if(_cara==2){ //Superior
				linea=_lineaX;
			}

			float angulo=0;


			//Soluciona algunos problemas cuando la linea se buggea
			if(linea<_renderizado._uncubo._dimension){
				angulo=_renderizado._uncubo.ajustarReferencias(_cara,linea);

				float angulogirado=angulo-_anguloinicial;


				//System.out.println("Angulo Girado: "+angulogirado);

				//Si es distinto de 0 es que a habido movimiento por lo tanto giramos la matriz
				if(angulogirado!=0)
				{
					//Aqui ahi que girar los datos del cubito (matriz) el numero de veces que sea necesario
					boolean pos=false;
					if(angulogirado<0)pos=true;

					//Nos dira el numero de veces que tenemos que girar la matriz

					_renderizado._uncubo.girarReferencias(_cara, linea, pos,angulogirado);


				}


			}

			//_renderizado._uncubo.imprimirReferencias();
		}


		return true;
	}




}
