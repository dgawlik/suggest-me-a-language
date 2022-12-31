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
            val splitterSorter = SplitterSorter()
            val feats = features.copyOf().toList()
            queue.addLast(Pair(root, feats))

            while (queue.isNotEmpty()) {
                val (node, candidateFeatures) = queue.removeFirst()
                val workArray = array.filter { compositeCriteria(node)(it) }

                if (workArray.isEmpty() || candidateFeatures.isEmpty()) {
                    continue
                }

                var minPenalty = 2.0
                var bestRule: FeatureRealization? = null
                for (candidate in candidateFeatures) {
                    val (_, _, splitVal, penalty) = splitterSorter.bestSplit(workArray, candidate)

                    if (penalty < minPenalty) {
                        minPenalty = penalty
                        bestRule = FeatureRealization(candidate, splitVal)
                    }
                }

                val newCandidates = candidateFeatures.filter { it.id != bestRule!!.feature.id }

                if (newCandidates.size < 35) {
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
    }
}