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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CuboRubik extends Activity {

	//TIPOS DE MENUS
	public final static int DIALOG_MENUPRINCIPAL_ID = 0;
	public final static int DIALOG_MENUPAUSA_ID = 1;
	public final static int DIALOG_MENUOPCIONES_ID = 2;

	//MODOS DE JUEGO
	public final static int MONTADO = 0;
	public final static int AZAR = 1;

	//OPCIONES DEL PROGRAMA
	public static final String OPCIONES = "OpcionesRubik";
	private int _dimensionCubo;
	private String _nombreUsuario;
	private boolean _espejoActivado;


	//Vista por defecto del cubo
	private GLSurfaceView _vista;

	/** Se crea cuando se hace el Activity */   
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);



		// Cargar Preferencias almacenadas
		SharedPreferences preferencias = getSharedPreferences(OPCIONES, 0);
		_dimensionCubo = preferencias.getInt("DimensionCubo", 3);
		_nombreUsuario = preferencias.getString("NombreUsuario", null);
		_espejoActivado = preferencias.getBoolean("EspejoActivado", true);

		
		// Quitamos el titulo
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Ponemos pantalla completa
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


		//Creamos la pantalla y la cargamos.
		_vista = new VistaCubo(this);



		setContentView(_vista);




	}


	protected Dialog onCreateDialog(int id) {
		final Dialog dialog;
		final Context mContext = getApplicationContext();
		switch(id) {
		case DIALOG_MENUPRINCIPAL_ID:

			dialog = new Dialog(this);
			dialog.setContentView(R.layout.mainmenu);
			dialog.setTitle("Menu Principal");
			dialog.show();

			
			dialog.setOnKeyListener(new OnKeyListener() {

				public boolean onKey(DialogInterface arg0, int arg1,
						KeyEvent key) {
					// Evitamos que si el cubo no esta creado, al puksar back no cierre el dialog.
					if (key.getKeyCode()==key.KEYCODE_BACK){
						if( ((VistaCubo)_vista)._renderizado._uncubo==null ) return true;
					}
					
					return false;
				}

			});
			
			//EditText del Nombre cargado de opciones (Anterior)
			EditText nombre = (EditText)dialog.findViewById(R.id.editNombre);
			nombre.setText(_nombreUsuario);
			
			//El spinner de la dimension carga de opciones su valor anterior.
			Spinner dimension = (Spinner)dialog.findViewById(R.id.spinnerDimension);
			dimension.setSelection(_dimensionCubo-1);

			//Boton Cubo Montado
			Button montado = (Button)dialog.findViewById(R.id.buttonMontado);
			montado.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					String dimension = ((Spinner)dialog.findViewById(R.id.spinnerDimension)).getSelectedItem().toString();

					String nombre = ((EditText)dialog.findViewById(R.id.editNombre)).getText().toString();

					if (nombre != null && nombre.trim().length() ==0)
					{   

						Toast toast = Toast.makeText(mContext, getString(R.string.errorusuarionull), Toast.LENGTH_LONG);
						toast.show();
					} else
					{
						((VistaCubo)_vista).crearObjetos( Integer.parseInt(dimension), nombre, MONTADO,_espejoActivado);
						dialog.cancel();
					}


				}
			});

			//Boton Al azar
			Button alAzar = (Button)dialog.findViewById(R.id.buttonAlAzar);
			alAzar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					String dimension = ((Spinner)dialog.findViewById(R.id.spinnerDimension)).getSelectedItem().toString();

					String nombre = ((EditText)dialog.findViewById(R.id.editNombre)).getText().toString();

					if (nombre != null && nombre.trim().length() ==0)
					{   

						Toast toast = Toast.makeText(mContext, getString(R.string.errorusuarionull), Toast.LENGTH_LONG);
						toast.show();
					} else
					{


						((VistaCubo)_vista).crearObjetos( Integer.parseInt(dimension), nombre, AZAR,_espejoActivado);
						dialog.cancel();
					}


				}
			});

			break;
		case DIALOG_MENUPAUSA_ID:

			dialog = new Dialog(this);

			dialog.setContentView(R.layout.pausemenu);
			dialog.setTitle("Pausa");
			dialog.show();

			//Boton Continuar
			Button continuar = (Button)dialog.findViewById(R.id.buttonContinuar);
			continuar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.cancel();
				}

			});


			//Boton Juego Nuevo
			Button juegoNuevo = (Button)dialog.findViewById(R.id.buttonJuegoNuevo);
			juegoNuevo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.cancel();
					showDialog(DIALOG_MENUPRINCIPAL_ID);
				}

			});


			//Boton Opciones
			Button opciones = (Button)dialog.findViewById(R.id.buttonOpciones);
			opciones.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//dialog.cancel();
					showDialog(DIALOG_MENUOPCIONES_ID);
				}

			});


			//Boton Salir
			Button salir = (Button)dialog.findViewById(R.id.buttonSalir);
			salir.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}

			});


			break;

		case DIALOG_MENUOPCIONES_ID:

			dialog = new Dialog(this);

			dialog.setContentView(R.layout.optionmenu);
			dialog.setTitle("Opciones");
			dialog.show();

			//Check Habilitar Espejo
			CheckBox espejo = (CheckBox)dialog.findViewById(R.id.checkActivarEspejo);
			espejo.setChecked(_espejoActivado);
			espejo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					((VistaCubo) _vista)._renderizado.toggleCamaraTrasera();
				}

			});

			break;


		default:
			dialog = null;
		}

		return dialog;
	}


	protected void onStart() {
		super.onStart();

	}


	@Override
	protected void onResume() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();
		_vista.onResume();

		if( ((VistaCubo)_vista)._renderizado._uncubo==null ) showDialog(DIALOG_MENUPRINCIPAL_ID);
		else showDialog(DIALOG_MENUPAUSA_ID);





	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		_vista.onPause();
	}
	
    @Override
    protected void onStop(){
       super.onStop();

      // Editamos las opciones
      SharedPreferences preferencias = getSharedPreferences(OPCIONES, 0);
      SharedPreferences.Editor editor = preferencias.edit();
      editor.putInt("DimensionCubo", ((VistaCubo)_vista)._renderizado._uncubo._dimension);
      editor.putString("NombreUsuario", ((VistaCubo)_vista)._nombreUsuario);
      editor.putBoolean("EspejoActivado", ((VistaCubo)_vista)._renderizado._camaratrasera);
      System.out.println("Dim: "+((VistaCubo)_vista)._renderizado._uncubo._dimension);
      System.out.println("Nombre: "+((VistaCubo)_vista)._nombreUsuario);
      System.out.println("Espejo: "+ ((VistaCubo)_vista)._renderizado._uncubo._dimension);
      // Las almacenamos
      editor.commit();
    }


}
