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
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CuboRubik extends Activity {

	//TIPOS DE MENUS
	public final static int DIALOG_MENUPRINCIPAL_ID = 0;
	public final static int DIALOG_MENUPAUSA_ID = 1;

	//MODOS DE JUEGO
	public final static int MONTADO = 0;
	public final static int AZAR = 1;



	//Vista por defecto del cubo
	private GLSurfaceView _vista;

	/** Se crea cuando se hace el Activity */   
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

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
						((VistaCubo)_vista).crearObjetos( Integer.parseInt(dimension), nombre, MONTADO);
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
						((VistaCubo)_vista).crearObjetos( Integer.parseInt(dimension), nombre, AZAR);
						dialog.cancel();
					}


				}
			});

			break;
		case DIALOG_MENUPAUSA_ID:

			dialog = new Dialog(this);

			dialog.setContentView(R.layout.optionmenu);
			dialog.setTitle("Opciones");
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
			
			//Boton Salir
			Button salir = (Button)dialog.findViewById(R.id.buttonSalir);
			salir.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
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

		showDialog(DIALOG_MENUPRINCIPAL_ID);



	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		_vista.onPause();
	}

}
