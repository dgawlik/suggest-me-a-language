/*
   == Suggest me a language, 2023 ==
*/

package main

import (
	"fmt"
	"math/rand"
	"time"
)

func fibonacci(n int) int {
	if n <= 1 {
		return 1
	} else {
		return fibonacci(n-1) + fibonacci(n-2)
	}
}

func produce(id int, queue chan int) {
	for {
		val := rand.Intn(30)
		fmt.Printf("Producer %d produced %d\n", id, val)
		queue <- val
		time.Sleep(500 * time.Millisecond)
	}
}

func consume(id int, queue chan int) {
	for work := range queue {
		fmt.Printf("Consumer %d, fibonacci(%d) = %d\n", id, work, fibonacci(work))
	}
}

func main() {
	ch := make(chan int, 10)

	for i := 1; i <= 2; i++ {
		go produce(i, ch)
	}

	for i := 1; i <= 5; i++ {
		go consume(i, ch)
	}

	time.Sleep(5 * time.Second)
}

// Running => go run Sample.go
