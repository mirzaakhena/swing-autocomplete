package com.mirzaakhena.swing.autocomplete;

import java.util.List;

/**
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Mirza Akhena
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * @author mirza.akhena@gmail.com
 *
 * @param <T>
 */
public interface Searchable<T> {

	/**
	 * 
	 * <pre>
	 * public List&lt;T&gt; search(String value) {
	 * 	List&lt;Pegawai&gt; listPegawai = pegawaiDao.findByName(value);
	 * 	return listPegawai;
	 * }
	 * </pre>
	 * 
	 * @param value
	 *            input value you want to search in your repository object
	 * @return List of Object that match your searching query / algorithm.
	 */
	public List<T> search(String value);

	/**
	 * 
	 * Put your search part here.</br> for the example you have class</br>
	 * 
	 * <pre>
	 * class Employee {
	 *   public String name;
	 *   public String address;
	 *   ...
	 * }
	 * </pre>
	 * 
	 * If You want to search "name" part you can do
	 * 
	 * <pre>
	 * public String getStringValue(Employee x) {
	 * 	return x.name;
	 * }
	 * </pre>
	 * 
	 * If You want to search "address" part you can do
	 * 
	 * <pre>
	 * public String getStringValue(Employee x) {
	 * 	return x.address;
	 * }
	 * </pre>
	 * 
	 * @param x
	 *            checked object
	 * @return String part of object
	 */
	public String getStringValue(T x);

	/**
	 * Invoke when object is selected.</br> WARNING! the value of x can be
	 * null.</br> You must check null value manualy
	 * 
	 * <pre>
	 * if (x != null) {
	 * 	// selected object ready to process here
	 * } else {
	 * 	// no object selected or found
	 * }
	 * </pre>
	 * 
	 * @param x
	 *            selected object. Can be null if no value match.
	 */
	public void selectEvent(T x);

}