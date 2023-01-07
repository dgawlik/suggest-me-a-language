#
# Suggest me a language, 2023
#

module Main where

minus (x:xs) (y:ys) = case (compare x y) of 
           LT -> x : minus  xs  (y:ys)
           EQ ->     minus  xs     ys 
           GT ->     minus (x:xs)  ys
minus  xs     _     = xs

primesToG m = 2 : sieve [3,5..m]
    where
    sieve (p:xs) 
       | p*p > m   = p : xs
       | otherwise = p : sieve (xs `minus` [p*p, p*p+2*p..])

main = do 
    print $ primesToG 100

# Running => ghc sample.hs && ./sample