package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends ArbolBinario<T>.Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
          super(elemento);
          this.altura=0;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        public String toString() {
          return elemento + " " + altura + "/" + balance(this);

        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
            return super.equals(vertice) && this.altura==vertice.altura;
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario. La complejidad en tiempo del método es <i>O</i>(log
     * <i>n</i>) garantizado.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeAVL v=verticeAVL(ultimoAgregado);
        rebalanceado(v);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo. La
     * complejidad en tiempo del método es <i>O</i>(log <i>n</i>) garantizado.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
      VerticeAVL v=verticeAVL(busca(raiz,elemento));
      if(v==null){
          return;
      }

      VerticeAVL hijo=new VerticeAVL(null);
      if(v.izquierdo!=null){
        VerticeAVL maximo=verticeAVL(maximoEnSubarbol(v.izquierdo));
        intercambia(maximo,v);
        v=maximo;
      }

      if(v.izquierdo==null && v.derecho==null){
        if (v==raiz) {
         raiz=null;
         ultimoAgregado =null;
     } else if (v.padre.izquierdo==v)
         v.padre.izquierdo=null;
       else
         v.padre.derecho=null;
     }
      else
      if(v.izquierdo!=null){
          v.izquierdo.padre=v.padre;
          if(v.padre.derecho==v)
            v.padre.derecho=v.izquierdo;
          else
            v.padre.izquierdo=v.izquierdo;
          hijo=verticeAVL(v.izquierdo);
      }else{
        v.derecho.padre=v.padre;
        if(v.padre.derecho==v)
          v.padre.derecho=v.derecho;
        else
          v.padre.izquierdo=v.derecho;
        hijo=verticeAVL(v.derecho);
      }

        rebalanceado(verticeAVL(v.padre));
        elementos--;

    }
    private void intercambia(Vertice ver,Vertice tice){
       T aux = ver.elemento;
      ver.elemento=tice.elemento;
      tice.elemento=aux;
    }
    //auxiliar para calcular balance
    private int balance(VerticeAVL vertice){
      if(vertice==null)
        return 0;
      return  getAltura(verticeAVL(vertice.izquierdo)) - getAltura(verticeAVL(vertice.derecho));
    }

    //Metodo que acualiza la altura
    private void actualizaAltura(VerticeAVL vertice){
      if (vertice==null)
         return;
       vertice.altura=Math.max(getAltura(verticeAVL(vertice.izquierdo)) , getAltura(verticeAVL(vertice.derecho))) + 1;
  }

    //auxiliar de rebalanceo
    private void rebalanceado(VerticeAVL v){
      if(v==null)
        return;
      VerticeAVL derecho=verticeAVL(v.derecho),izquierdo=verticeAVL(v.izquierdo);
      actualizaAltura(v);
      if(balance(v)==-2){
        if(balance(derecho)==1){
          super.giraDerecha(derecho);
          actualizaAltura(derecho);
          actualizaAltura(verticeAVL(derecho.padre));
        }
        super.giraIzquierda(v);
      }
      if(balance(v)==2){
        if(balance(izquierdo)==-1){
          super.giraIzquierda(izquierdo);
          actualizaAltura(izquierdo);
          actualizaAltura(verticeAVL(izquierdo.padre));
        }
        super.giraDerecha(v);
      }
      actualizaAltura(v);
      rebalanceado(verticeAVL(v.padre));
    }

    /**
     * Regresa la altura del vértice AVL.
     * @param vertice el vértice del que queremos la altura.
     * @return la altura del vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    public int getAltura(VerticeArbolBinario<T> vertice) {
      if(vertice ==null)
        return -1;
      return verticeAVL(vertice).altura;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeAVL}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice AVL.
     * @return el vértice recibido visto como vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    protected VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }
}

