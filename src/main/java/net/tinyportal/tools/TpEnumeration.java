/*
    This file is part of tPortal.

    tPortal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    tPortal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with tPortal.  If not, see <http://www.gnu.org/licenses/>.

    The original code was written by Sebastien Bettinger <sebastien.bettinger@gmail.com>

 */

package net.tinyportal.tools;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Simple implémentation d'une enumération à partir d'une liste
 * @author sebastien.bettinger@gmail.com
 *
 * @param <E>
 */
public class TpEnumeration<E> implements Enumeration<E> {
	List<E> l;
	int counter;
	
	public TpEnumeration(List<E> list) {
		l = list;
		if (l == null) {
			l = new ArrayList<E>();
		}
		counter = 0;
	}
	
	@Override
	public boolean hasMoreElements() {
		return counter<l.size();
	}

	@Override
	public E nextElement() {
		return l.get(counter++);
	}

}
