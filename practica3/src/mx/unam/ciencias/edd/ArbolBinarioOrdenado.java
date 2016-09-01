package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para emular la pila de ejecución. */
        private Pila<ArbolBinario<T>.Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
          pila= new Pila<ArbolBinario<T>.Vertice>();
          rellena(raiz);
        }
        private void rellena(Vertice v){
          if(v==null)
          return;
          rellena(v.derecho);
          pila.mete(v);
          rellena(v.izquierdo);
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
          if(pila.esVacia())
            return false;
          return true;
        }

        /* Regresa el siguiente elemento del árbol en orden. */
        @Override public T next() {
          return  pila.saca().elemento;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {

      Vertice aux= nuevoVertice(elemento);
      elementos++;
      ultimoAgregado=aux;
      if(raiz==null)
        raiz=aux;
     else
        agrega(raiz,aux);
  }
  private void agrega(Vertice v, Vertice e) {

      if(e.elemento.compareTo(v.elemento)<=0){
         if(v.izquierdo==null){
          v.izquierdo=e;
          e.padre=v;
        } else{
          agrega(v.izquierdo,e);}
        }
        else{
          if(v.derecho==null){
            v.derecho=e;
            e.padre=v;
          }
          else{
            agrega(v.derecho,e);}
}



}

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
      Vertice eliminar=busca(raiz,elemento);
      if(eliminar==null)
      return;
        if(raiz==eliminar && elementos==1)
        raiz=null;
        else
        if (eliminar.derecho==null && eliminar.izquierdo==null)
          if(eliminar.padre.derecho==eliminar)
              eliminar.padre.derecho=null;
            else
              eliminar.padre.izquierdo=null;
          else
          if(eliminar.izquierdo==null){
            if(eliminar.padre==null){
                raiz=eliminar.derecho;
                raiz.padre=null;
            } else{
              eliminar.derecho.padre=eliminar.padre;
              if(eliminar.padre.izquierdo==eliminar)
                eliminar.padre.izquierdo=eliminar.derecho;
              else
                eliminar.padre.derecho=eliminar.derecho;
              }
          }
        else{
         Vertice aux=maximoEnSubarbol(eliminar.izquierdo);
         eliminar.elemento=aux.elemento;
         if(aux.padre.izquierdo==aux)
           aux.padre.izquierdo=aux.izquierdo;
         else
           aux.padre.derecho=aux.izquierdo;
         if(aux.izquierdo!=null)
           aux.izquierdo.padre=aux.padre;
      }

    if(ultimoAgregado==eliminar)
      ultimoAgregado=null;
    elementos--;
  }


    /**
     * Busca recursivamente un elemento, a partir del vértice recibido.
     * @param vertice el vértice a partir del cuál comenzar la búsqueda. Puede
     *                ser <code>null</code>.
     * @param elemento el elemento a buscar a partir del vértice.
     * @return el vértice que contiene el elemento a buscar, si se encuentra en
     *         el árbol; <code>null</code> en otro caso.
     */
    @Override protected Vertice busca(Vertice vertice, T elemento) {

      if (vertice==null)
        return null;
      if((vertice.elemento.compareTo(elemento))==0)
        return vertice;
      if(elemento.compareTo(vertice.elemento)<0)
        return busca(vertice.izquierdo,elemento);
      return busca(vertice.derecho, elemento);



    }

    /**
     * Regresa el vértice máximo en el subárbol cuya raíz es el vértice que
     * recibe.
     * @param vertice el vértice raíz del subárbol del que queremos encontrar el
     *                máximo.
     * @return el vértice máximo el subárbol cuya raíz es el vértice que recibe.
     */
    protected Vertice maximoEnSubarbol(Vertice vertice) {

      if(vertice.derecho==null)
        return vertice;
      return maximoEnSubarbol(vertice.derecho);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
      Vertice ver=vertice(vertice); //esta muy gracioso XD
      if(ver.izquierdo==null)
        return;
      Vertice tice=ver.izquierdo;
      tice.padre=ver.padre;
      if(ver.padre!=null)
          if(ver.padre.derecho==ver)
            ver.padre.derecho=tice;
          else
            ver.padre.izquierdo=tice;
        else
          raiz=tice;
        ver.izquierdo=tice.derecho;
        if(tice.derecho!=null)
          tice.derecho.padre=ver;
        tice.derecho=ver;
        ver.padre=tice;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
      Vertice ver=vertice(vertice); //esta muy gracioso XD
      if(ver.derecho==null)
        return;
      Vertice tice=ver.derecho;
      tice.padre=ver.padre;
      if(ver.padre!=null)
          if(ver.padre.izquierdo==ver)
            ver.padre.izquierdo=tice;
          else
            ver.padre.derecho=tice;
        else
          raiz=tice;
        ver.derecho=tice.izquierdo;
        if(tice.izquierdo!=null)
          tice.izquierdo.padre=ver;
        tice.izquierdo=ver;
        ver.padre=tice;
    }
}

