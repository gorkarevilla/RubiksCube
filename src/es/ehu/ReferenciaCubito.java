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

package es.ehu;

public class ReferenciaCubito {
	public int _x;
	public int _y;
	public int _z;

	public ReferenciaCubito(int x,int y,int z)
	{
		_x=x;
		_y=y;
		_z=z;
		//System.out.println("x:"+x+"y:"+y+"z:"+z);
	}
	
	@Deprecated
	public void imprimirReferencia()
	{
		System.out.print(" ("+_x+","+_y+","+_z+") ");
	}

}