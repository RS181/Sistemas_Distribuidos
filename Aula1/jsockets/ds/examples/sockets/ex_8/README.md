# Como correr programa

**No terminal 1 (iniciar CalculatorMultiServer)**

    PORTA SERVIDOR DEFAULT: 44444

    $ pwd 
    /.../jsockets
    $ java ds.examples.sockets.ex_8.CalculatorMultiServer localhost

**No terminal 2 (iniciar os peer's e respetiva comunicacao)** 

    $ pwd
    /.../jsockets
    $ java ds.examples.sockets.ex_8.Start


TODO: Melhorar os Logs (tornar mais percetiveis e colocar apenas logs necessarios)
TODO: How would you generalize the solution for a ring with n peers?