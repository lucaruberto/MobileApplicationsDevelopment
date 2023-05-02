data class MinesweeperBoard constructor(val campo: List<String>) {

    val righe by lazy {
        campo.size
    }
    val colonne by lazy {
        campo[0].length
    }

    fun withNumbers(): List<String> {

        

    }

    private fun changeCamp(old:Array<IntArray>, row:Int, column:Int):Array<IntArray>{

        for (i in old.indices)
        {
            for (j in old[i].indices)
            {
               if (old[i][j]==-1)
               {
                  if(i==0)
                  {
                      if(j==0)
                      {
                          old[i][j+1]=old[i][j+1]+1;
                          old[i+1][j]=old[i+1][j]+1;
                          old[i+1][j+1]=old[i+1][j+1]+1;

                      }
                      else if (j== old[i].size-1)
                      {
                          old[i][j-1]= old[i][j-1]+1;
                          old[i+1][j]=old[i+1][j]+1;
                          old[i+1][j-1]= old[i+1][j-1]+1;
                      }
                      else
                      {
                          old[i][j+1]=old[i][j+1]+1;
                          old[i][j-1]=old[i][j-1]+1;
                          old[i+1][j]=old[i+1][j]+1;
                          old[i+1][j+1]=old[i+1][j+1]+1;
                          old[i+1][j-1]=old[i+1][j-1]+1;

                      }
                  }
                  else if(i==old.size-1)
                   {
                       if(j==0)
                       {
                           old[i][j+1]=old[i][j+1]+1;
                           old[i-1][j]=old[i-1][j]+1;
                           old[i-1][j+1]=old[i-1][j+1]+1;

                       }
                       else if (j== old[i].size-1)
                       {
                           old[i][j-1]= old[i][j-1]+1;
                           old[i-1][j]=old[i-1][j]+1;
                           old[i-1][j-1]= old[i-1][j-1]+1;
                       }
                       else
                       {
                           old[i][j+1]=old[i][j+1]+1;
                           old[i][j-1]=old[i][j-1]+1;
                           old[i-1][j]=old[i-1][j]+1;
                           old[i-1][j+1]=old[i-1][j+1];
                           old[i-1][j-1]=old[i-1][j-1];

                       }
                   }
                  else
                  {
                      if(j==0)
                      {
                          old[i][j+1]=old[i][j+1]+1;
                          old[i-1][j]=old[i-1][j]+1;
                          old[i-1][j+1]=old[i-1][j+1]+1;
                          old[i+1][j+1]=old[i+1][j+1]+1;
                          old[i+1][j]= old[i+1][j]+1;

                      }
                      else if (j== old[i].size-1)
                      {
                          old[i][j-1]= old[i][j-1]+1;
                          old[i-1][j]=old[i-1][j]+1;
                          old[i-1][j-1]= old[i-1][j-1]+1;
                          old[i+1][j]= old[i+1][j]+1;
                          old[i+1][j-1]=old[i+1][j-1]+1;
                      }
                      else
                      {
                          old[i][j+1]=old[i][j+1]+1;
                          old[i][j-1]=old[i][j-1]+1;
                          old[i-1][j]=old[i-1][j]+1;
                          old[i-1][j+1]=old[i-1][j+1]+1;
                          old[i-1][j-1]=old[i-1][j-1]+1;
                          old[i+1][j]=old[i+1][j]+1;
                          old[i+1][j+1]=old[i+1][j+1]+1;
                          old[i+1][j-1]=old[i+1][j-1]+1;

                      }

                  }


               }

            }
        }
        println(old);
       return old;
    }
}
