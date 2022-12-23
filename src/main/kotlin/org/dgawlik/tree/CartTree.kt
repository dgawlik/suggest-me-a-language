package org.dgawlik.tree

import org.dgawlik.domain.Feature
import org.dgawlik.domain.FeatureRealization
import org.dgawlik.domain.Language


class CartTree(array: Array<Language>, features: Array<Feature>) {

    var root = TreeNode(null, null, null, null)

    init {
        val queue = ArrayDeque<TreeNode>()
        val spSor = SplitterSorter()
        var candidateFeatures = features.copyOf()
        queue.addLast(root)

        while (queue.isNotEmpty() && candidateFeatures.isNotEmpty()) {
            val node = queue.removeFirst();
            val workArray = array.filter { compositeCriteria(node)(it) }.toTypedArray()

            var minEntropy = Double.MAX_VALUE
            var bestRule: FeatureRealization? = null
            for (candidate in candidateFeatures) {
                val (lhs, rhs, _, splitVal) = spSor.bestSplit(workArray, candidate)

                val lhsScore = totalEntropy(lhs, candidateFeatures)
                val rhsScore = totalEntropy(rhs, candidateFeatures)

                if(lhsScore + rhsScore < minEntropy) {
                    minEntropy = lhsScore + rhsScore
                    bestRule = FeatureRealization(candidate, splitVal)
                }
            }

            candidateFeatures = candidateFeatures.filter { it.id != bestRule!!.feature.id }.toTypedArray()

            val leftChild = TreeNode(node, null, null, null)
            val rightChild = TreeNode(node, null, null, null)

            node.left = leftChild
            node.right = rightChild
            node.rule = bestRule

            queue.addLast(leftChild)
            queue.addLast(rightChild)
        }

    }

    fun compositeCriteria(node: TreeNode): (Language) -> Boolean {
        var checks = arrayOf<(Language) -> Boolean>()

        var it: TreeNode? = node
        while (it != null) {
            val rule = it.rule
            if (rule != null) {
                checks += { lang: Language ->
                    val value = lang.features.find { it.feature.id == rule.feature.id }?.value ?: 0
                    value < rule.value
                }
            }
            it = it.parent
        }

        return { lang ->
            if(checks.isEmpty()){
                true
            }
            else {
                checks.all { it(lang) } }
            }
    }

    fun totalEntropy(langs: Array<Language>, features: Array<Feature>): Double {
        var sum = 0.0
        for ( ft in features) {
            sum += Histogram(ft, langs).entropy()
        }
        return sum
    }

}