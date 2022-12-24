package org.dgawlik.tree

import org.dgawlik.domain.Feature
import org.dgawlik.domain.FeatureRealization
import org.dgawlik.domain.Language


class Id3Tree(array: Array<Language>, features: Array<Feature>) {

    val root = buildTree(array, features)

    companion object {

        fun buildTree(array: Array<Language>, features: Array<Feature>): TreeNode {

            val root = TreeNode(null, null, null, null)
            val queue = ArrayDeque<Pair<TreeNode, List<Feature>>>()
            val spSor = SplitterSorter()
            var feats = features.copyOf().toList()
            queue.addLast(Pair(root, feats))

            while (queue.isNotEmpty()) {
                val (node, candidateFeatures) = queue.removeFirst();
                val workArray = array.filter { compositeCriteria(node)(it) }

                if (workArray.isEmpty() || candidateFeatures.isEmpty()) {
                    continue
                }

                var minEntropy = Double.MAX_VALUE
                var bestRule: FeatureRealization? = null
                for (candidate in candidateFeatures) {
                    val (lhs, rhs, splitVal) = spSor.bestSplit(workArray, candidate)

                    val lhsScore = totalEntropy(lhs, candidateFeatures)
                    val rhsScore = totalEntropy(rhs, candidateFeatures)

                    if (lhsScore + rhsScore < minEntropy) {
                        minEntropy = lhsScore + rhsScore
                        bestRule = FeatureRealization(candidate, splitVal)
                    }
                }

                val newCandidates = candidateFeatures.filter { it.id != bestRule!!.feature.id }

                if (newCandidates.size < 34) {
                    continue
                }

                val leftChild = TreeNode(node, null, null, null)
                val rightChild = TreeNode(node, null, null, null)

                node.left = leftChild
                node.right = rightChild
                node.rule = bestRule

                queue.addLast(Pair(leftChild, newCandidates))
                queue.addLast(Pair(rightChild, newCandidates))
            }

            return root
        }

        fun compositeCriteria(node: TreeNode): (Language) -> Boolean {
            var checks = arrayOf<(Language) -> Boolean>()

            var it: TreeNode? = node
            var parent: TreeNode? = node.parent
            while (parent !== null) {
                val rule = parent.rule

                if (parent.right == it) {
                    checks += { lang: Language ->
                        val value = lang.features.find { it.feature.id == rule!!.feature.id }?.value ?: 0
                        value >= rule!!.value
                    }
                }
                parent = parent.parent
                it = it?.parent
            }

            return { lang ->
                if (checks.isEmpty()) {
                    true
                } else {
                    checks.all { it(lang) }
                }
            }
        }

        fun totalEntropy(langs: List<Language>, features: List<Feature>): Double {
            var sum = 0.0
            for (ft in features) {
                sum += Histogram(ft, langs).entropy()
            }
            return sum
        }
    }
}