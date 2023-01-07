/*
    == Suggest me a language, 2023 ==
*/


#include <iostream>

template <typename T>
struct Node {
    T value;
    Node* prev;
    Node* next;

    Node(T value) {
        this->value = value;
    }
};

template <typename T>
class LinkedList {
    public:
    LinkedList() {
        this->start = new Node<T>(T{});
        this->end = new Node<T>(T{});

        this->start->next = this->end;
        this->end->prev = this->start;
    }

    bool add(T value, int index) {
        Node<T>* it = this->start;
        while(index-- && it != this->end){
            it = it->next;
        }

        if(it == this->end){
            return false;
        }

        Node<T>* newNode = new Node<T>(value);
        newNode->next = it->next;
        newNode->prev = it;
        it->next->prev = newNode;
        it->next = newNode;

        return true;
    }

    bool remove(int index) {
        Node<T>* it = this->start;
        while(index-- && it->next != this->end){
            it = it->next;
        }

        if(it->next != this->end) {
            Node<T>* tmp = it->next;
            it->next->next->prev = it;
            it->next = it->next->next;
            delete tmp;
        }

        
    }

    void print() {
        std::cout << "[";
        Node<T>* it = this->start->next;
        while(it != this->end){
            std::cout << it->value << ", ";
            it = it->next;
        }
        std::cout << "]\n";
    }

    private:
    Node<T>* start;
    Node<T>* end;
};


int main() {
    LinkedList<double> lst;

    lst.add(1.0, 0);
    lst.add(2.0, 1);
    lst.add(3.0, 0);
    lst.add(10.0, 3);
    lst.remove(1);

    lst.print();

    return 0;
}

// Running => gcc -o sample sample.cxx -lstdc++ && ./sample