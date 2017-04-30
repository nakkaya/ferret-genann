ferret-genann
=============

Ferret bindings for genann - simple neural network library in ANSI C https://codeplea.com/genann


    (require '[ferret-genann.core :as nn])

    (def network (nn/init 2 1 2 1))

    (dotimes [_ 30000]
      (nn/train network (list 0 0) (list 0) 3)
      (nn/train network (list 0 1) (list 1) 3)
      (nn/train network (list 1 0) (list 1) 3)
      (nn/train network (list 1 1) (list 0) 3))

    (println (nn/run network (list 0 0))
             (nn/run network (list 0 1))
             (nn/run network (list 1 0))
             (nn/run network (list 1 1)))